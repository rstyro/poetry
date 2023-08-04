package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rstyro.poetry.entity.PoetryHuaJianJiVo;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class HuaJianJiHandler implements BaseHandler{

    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    public void handler(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.stream(files).filter(i->i.getName().endsWith(".json") && i.getName().contains("-juan")).forEach(f -> {
            try {
                FileReader fileReader = new FileReader(f);
                String json = fileReader.readString();
                List<PoetryHuaJianJiVo> data = JSON.parseArray(json, PoetryHuaJianJiVo.class);
                List<List<PoetryHuaJianJiVo>> splitList = ListUtil.split(data, 1000);
                CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
                splitList.stream().forEach(split -> {
                    if (executorService.getActiveCount() < core) {
                        executorService.execute(() -> {
                            batchSavePoetry(split, countDownLatch);
                        });
                    } else {
                        batchSavePoetry(split, countDownLatch);
                    }
                });
                countDownLatch.await();
                log.info("处理完文件：{}",f.getName());
            } catch (InterruptedException e) {
                log.error(e.getMessage(),e);
            }
        });
        log.info("处理文件结束...");
    }

    public void batchSavePoetry(List<PoetryHuaJianJiVo> list, CountDownLatch countDownLatch) {
        try {
            HashSet<String> dynasty = new HashSet<>(Arrays.asList("五代后蜀"));
            HashSet<String> type = new HashSet<>(Arrays.asList("花间集"));
            HashSet<String> tags = new HashSet<>(Arrays.asList("花间集"));
            List<PoetryIndex> dataList = new ArrayList<>();
            list.forEach(d -> {
                PoetryIndex index = new PoetryIndex();
                index.setAuthor(d.getAuthor());
                index.setContent(d.getParagraphs());
                index.setTitle(d.getTitle());
                index.setSection(d.getRhythmic());
                index.setTags(tags);
                index.setType(type);
                index.setDynasty(dynasty);
                index.setTranslations(d.getNotes());
                index.set_id(getMd5Id(index));
                dataList.add(index);
            });
            poetryEsService.batchSaveDoc(dataList);

            // 保存到数据库
            savePoetryToDb(dataList);
        } catch (Exception e) {
            log.error("保存数据时报错，err={}", e.getMessage(), e);
        } finally {
            countDownLatch.countDown();
        }
    }

}
