package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.rstyro.poetry.entity.PoetryTangSongVo;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class TangSongHandler {

    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    public void handler(String filePath){
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.stream(files).filter(f->f.getName().contains("poet")).forEach(f->{
            FileReader fileReader = new FileReader(f);
            String json = fileReader.readString();
            List<PoetryTangSongVo> list = JSON.parseArray(json, PoetryTangSongVo.class);
            List<List<PoetryTangSongVo>> splitList = ListUtil.split(list, 1000);
            splitList.stream().forEach(split->{
                try {
                    List<PoetryIndex> dataList = new ArrayList<>();
                    split.forEach(d->{
                        PoetryIndex index = new PoetryIndex();
                        index.setAuthor(d.getAuthor());
                        index.setContent(d.getParagraphs());
                        index.setTitle(d.getTitle());
                        index.setSection(d.getVolume());
                        index.setTags(d.getTags());
                        dataList.add(index);
                    });
                    poetryEsService.batchSaveDoc(dataList);
                }catch (Exception e){
                    log.error("保存楚辞数据时报错，err={}",e.getMessage(),e);
                }

            });
        });
    }

}
