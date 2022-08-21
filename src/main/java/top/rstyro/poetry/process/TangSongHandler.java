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
        Arrays.stream(files).filter(f->f.getName().contains("poet")
                || f.getName().contains("唐诗")).forEach(f->{
            FileReader fileReader = new FileReader(f);
            String json = fileReader.readString();
            String dynasty = "唐朝";
            if(f.getName().contains("song")){
                dynasty = "宋朝";
            }
            List<PoetryTangSongVo> list = JSON.parseArray(json, PoetryTangSongVo.class);
            List<List<PoetryTangSongVo>> splitList = ListUtil.split(list, 1000);
            Set<String> dynastyList = new HashSet<>();
            dynastyList.add(dynasty);
            splitList.stream().forEach(split->{
                try {
                    List<PoetryIndex> dataList = new ArrayList<>();
                    split.forEach(d->{
                        List<String> contentList = d.getParagraphs();
                        if(!ObjectUtils.isEmpty(contentList)){
                            Set<String> type = new HashSet<>();
                            String line = contentList.get(0);
                            int length = line.split("，")[0].trim().length();
                            if(length == 5){
                                type.add("五言诗");
                            }else if(length == 7){
                                type.add("七言诗");
                            }else {
                                type.add("其他");
                            }
                            PoetryIndex index = new PoetryIndex();
                            index.setAuthor(Tools.cnToSimple(d.getAuthor()));
                            index.setContent(Tools.cnToSimple(d.getParagraphs()));
                            index.setTitle(Tools.cnToSimple(d.getTitle()));
                            index.setSection(Tools.cnToSimple(d.getVolume()));
                            index.setTags(new HashSet<>(Tools.cnToSimple(d.getTags())));
                            index.setDynasty(dynastyList);
                            index.setType(type);
                            dataList.add(index);
                        }
                    });
                    poetryEsService.batchSaveDoc(dataList);
                }catch (Exception e){
                    log.error("保存楚辞数据时报错，err={}",e.getMessage(),e);
                }

            });
        });
    }

}
