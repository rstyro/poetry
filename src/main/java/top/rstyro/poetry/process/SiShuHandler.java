package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.rstyro.poetry.entity.PoetrySiShuVo;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.util.Tools;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class SiShuHandler implements BaseHandler{

    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    public void handler(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.stream(files).filter(i->i.getName().endsWith(".json")).forEach(f -> {
            try {
                FileReader fileReader = new FileReader(f);
                String json = fileReader.readString();
                String name = f.getName();
                String au = "";
                Set<String> dynastyList = new HashSet<>();
                HashSet<String> tags = new HashSet<>();
                tags.add("四书之一");
                dynastyList.add("春秋战国时期");
                if(name.contains("lunyu")){
                    au="孔子的弟子及其再传弟子";
                }else if(name.contains("daxue")){
                    au="曾子";
                }else if(name.contains("mengzi")){
                    au="孟子其弟子及其再传弟子";
                }else if(name.contains("zhongyong")){
                    au="子思";
                }else {
                    throw new RuntimeException("命名规范点");
                }
                List<PoetrySiShuVo> data = JSON.parseArray(json, PoetrySiShuVo.class);
                List<List<PoetrySiShuVo>> splitList = ListUtil.split(data, 1000);
                CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
                String finalAu = au;
                splitList.stream().forEach(split -> {
                    if (executorService.getActiveCount() < core) {
                        executorService.execute(() -> {
                            batchSavePoetry(split, finalAu,dynastyList,tags, countDownLatch);
                        });
                    } else {
                        batchSavePoetry(split,finalAu,dynastyList,tags, countDownLatch);
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

    public void batchSavePoetry(List<PoetrySiShuVo> list, String au, Set<String> dynastyList, Set<String> tags, CountDownLatch countDownLatch) {
        try {
            List<PoetryIndex> dataList = new ArrayList<>();
            Set<String> type = new HashSet<>();
            type.add("四书五经");
            list.forEach(d -> {
                List<String> contentList = d.getParagraphs();
                if (!ObjectUtils.isEmpty(contentList)) {
                    PoetryIndex index = new PoetryIndex();
                    index.setAuthor(au);
                    index.setContent(Tools.cnToSimple(d.getParagraphs()));
                    index.setTitle(Tools.cnToSimple(d.getChapter()));
                    index.setTags(tags);
                    index.setDynasty(dynastyList);
                    index.setType(type);
                    index.set_id(getMd5Id(index));
                    dataList.add(index);
                }
            });
            poetryEsService.batchSaveDoc(dataList);
        } catch (Exception e) {
            log.error("保存报错，err={}", e.getMessage(), e);
        } finally {
            countDownLatch.countDown();
        }
    }

}
