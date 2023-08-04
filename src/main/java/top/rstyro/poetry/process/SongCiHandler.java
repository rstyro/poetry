package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.rstyro.poetry.entity.PoetrySongCiVo;
import top.rstyro.poetry.entity.PoetryTangSongVo;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.util.Tools;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class SongCiHandler implements BaseHandler{

    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    public void handler(String filePath){
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.stream(files).filter(f->f.getName().contains("ci.song")
                || f.getName().contains("宋词")).forEach(f->{
            FileReader fileReader = new FileReader(f);
            String json = fileReader.readString();
            List<PoetrySongCiVo> list = JSON.parseArray(json, PoetrySongCiVo.class);
            List<List<PoetrySongCiVo>> splitList = ListUtil.split(list, 100);
            Set<String> dynastyList = new HashSet<>();
            dynastyList.add("宋朝");
            CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
            splitList.stream().forEach(split->{
                if(executorService.getActiveCount()<core){
                    executorService.execute(()->{
                        batchSavePoetry(split,dynastyList,countDownLatch);
                    });
                }else {
                    batchSavePoetry(split,dynastyList,countDownLatch);
                }
            });
            log.info("处理完文件={}",f.getName());
        });
    }

    public void batchSavePoetry(List<PoetrySongCiVo> list,Set<String> dynastyList, CountDownLatch countDownLatch) {
        try {
            List<PoetryIndex> dataList = new ArrayList<>();
            HashSet<String> tags = new HashSet<>(Arrays.asList("宋词"));
            Set<String> type = new HashSet<>();
            type.add("宋词");
            list.forEach(d->{
                List<String> contentList = d.getParagraphs();
                if(!ObjectUtils.isEmpty(contentList)){
                    PoetryIndex index = new PoetryIndex();
                    index.setAuthor(Tools.cnToSimple(d.getAuthor()));
                    index.setContent(Tools.cnToSimple(d.getParagraphs()));
                    index.setTitle(Tools.cnToSimple(d.getRhythmic()));
                    index.setTags(tags);
                    index.setDynasty(dynastyList);
                    index.setType(type);
                    index.set_id(getMd5Id(index));
                    dataList.add(index);
                }
            });
            poetryEsService.batchSaveDoc(dataList);


            // 保存到数据库
            savePoetryToDb(dataList);
        }catch (Exception e){
            log.error("保存数据时报错，err={}",e.getMessage(),e);
        }finally {
            countDownLatch.countDown();
        }
    }

}
