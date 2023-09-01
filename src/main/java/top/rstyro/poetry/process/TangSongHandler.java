package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
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
public class TangSongHandler implements BaseHandler {

    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    public void handler(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.stream(files).filter(f -> f.getName().contains("poet")
                || f.getName().contains("唐诗") || StrUtil.isNumeric(f.getName().replaceAll(".json", ""))).forEach(f -> {
            try {
                FileReader fileReader = new FileReader(f);
                String json = fileReader.readString();
                String dynasty = "唐朝";
                if (f.getName().contains("song")) {
                    dynasty = "宋朝";
                }
                List<PoetryTangSongVo> list = JSON.parseArray(json, PoetryTangSongVo.class);
                List<List<PoetryTangSongVo>> splitList = ListUtil.split(list, 1000);
                Set<String> dynastyList = new HashSet<>();
                dynastyList.add(dynasty);

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
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("处理完文件={}", f.getName());
        });
    }

    public void batchSavePoetry(List<PoetryTangSongVo> list, Set<String> dynastyList, CountDownLatch countDownLatch) {
        try {
            List<PoetryIndex> dataList = new ArrayList<>();
            Set<String> type = new HashSet<>();
            type.add("诗词");
            list.forEach(d -> {
                List<String> contentList = d.getParagraphs();
                if (!ObjectUtils.isEmpty(contentList)) {
                    PoetryIndex index = new PoetryIndex();
                    index.setAuthor(Tools.cnToSimple(d.getAuthor()));
                    index.setContent(Tools.cnToSimple(d.getParagraphs()));
                    index.setTitle(Tools.cnToSimple(d.getTitle()));
                    index.setSection(Tools.cnToSimple(d.getVolume()));
                    index.setTags(new HashSet<>(Tools.cnToSimple(d.getTags())));
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
            log.error("保存数据时报错，err={}", e.getMessage(), e);
        } finally {
            countDownLatch.countDown();
        }
    }

    /**
     * 判断是否工整
     *
     * @return
     */
    public boolean isNeat(List<String> content) {
        int linLength = -1;
        for (String line : content) {
            if (linLength != -1 && linLength != line.length()) {
                return false;
            } else {
                linLength = line.length();
            }
        }
        return false;
    }

}
