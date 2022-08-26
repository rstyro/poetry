package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rstyro.poetry.entity.PoetryChuCiVo;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
public class ChuCiHandler implements BaseHandler{

    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    public void handler(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.stream(files).forEach(f -> {
            try {
                FileReader fileReader = new FileReader(f);
                String json = fileReader.readString();
                List<PoetryChuCiVo> data = JSON.parseArray(json, PoetryChuCiVo.class);
                List<List<PoetryChuCiVo>> splitList = ListUtil.split(data, 1000);
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

    public void batchSavePoetry(List<PoetryChuCiVo> list, CountDownLatch countDownLatch) {
        try {
            HashSet<String> dynasty = new HashSet<>(Arrays.asList("战国时期"));
            HashSet<String> type = new HashSet<>(Arrays.asList("楚辞"));
            HashSet<String> tags = new HashSet<>(Arrays.asList("楚辞"));
            List<PoetryIndex> dataList = new ArrayList<>();
            list.forEach(d -> {
                PoetryIndex index = new PoetryIndex();
                index.setAuthor(d.getAuthor());
                index.setContent(d.getContent());
                index.setTitle(d.getTitle());
                index.setSection(d.getSection());
                index.setChapter(d.getChapter());
                index.setTags(tags);
                index.setType(type);
                index.setDynasty(dynasty);
                index.set_id(getMd5Id(index));
                dataList.add(index);
            });
            poetryEsService.batchSaveDoc(dataList);
        } catch (Exception e) {
            log.error("保存楚辞数据时报错，err={}", e.getMessage(), e);
        } finally {
            countDownLatch.countDown();
        }
    }

}
