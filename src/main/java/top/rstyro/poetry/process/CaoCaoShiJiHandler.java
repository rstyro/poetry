package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.rstyro.poetry.entity.PoetryTangSongVo;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.util.Tools;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class CaoCaoShiJiHandler implements BaseHandler {

    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    public void handler(String filePath) {
        try {
            File f = new File(filePath);
            FileReader fileReader = new FileReader(f);
            String json = fileReader.readString();
            List<PoetryTangSongVo> list = JSON.parseArray(json, PoetryTangSongVo.class);
            List<List<PoetryTangSongVo>> splitList = ListUtil.split(list, 1000);
            Set<String> dynastyList = new HashSet<>();
            dynastyList.add("东汉末年");
            dynastyList.add("三国时期");
            CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
            splitList.stream().forEach(split -> {
                if (executorService.getActiveCount() < core) {
                    executorService.execute(() -> {
                        batchSavePoetry(split, dynastyList, countDownLatch);
                    });
                } else {
                    batchSavePoetry(split, dynastyList, countDownLatch);
                }
            });
            countDownLatch.await();
            log.info("处理完文件={}", f.getName());
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }

    public void batchSavePoetry(List<PoetryTangSongVo> list, Set<String> dynastyList, CountDownLatch countDownLatch) {
        try {
            List<PoetryIndex> dataList = new ArrayList<>();
            Set<String> type = new HashSet<>();
            type.add("曹操诗集");
            list.forEach(d -> {
                List<String> contentList = d.getParagraphs();
                if (!ObjectUtils.isEmpty(contentList)) {
                    PoetryIndex index = new PoetryIndex();
                    index.setAuthor("曹操");
                    index.setContent(Tools.cnToSimple(d.getParagraphs()));
                    index.setTitle(Tools.cnToSimple(d.getTitle()));
                    index.setTags(new HashSet<>(Arrays.asList("古诗")));
                    index.setDynasty(dynastyList);
                    index.setType(type);
                    index.set_id(getMd5Id(index));
                    dataList.add(index);
                }
            });
            poetryEsService.batchSaveDoc(dataList);

            // 保存到数据库
            savePoetryToDb(dataList);
        } catch (Exception e) {
            log.error("保存曹操诗集报错，err={}", e.getMessage(), e);
        } finally {
            countDownLatch.countDown();
        }
    }

}
