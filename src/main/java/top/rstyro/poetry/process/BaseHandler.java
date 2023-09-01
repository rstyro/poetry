package top.rstyro.poetry.process;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import top.rstyro.poetry.db.entity.Poetrys;
import top.rstyro.poetry.db.service.IPoetrysService;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.util.BeanContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public interface BaseHandler {
    int core = 8;
    ThreadPoolExecutor executorService = new ThreadPoolExecutor(core, core, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000));
    void handler(String filePath);

    default String getMd5Id(PoetryIndex index){
        index.setCreate_time(LocalDateTime.now());
        PoetryIndex poetryIndex = new PoetryIndex();
        // 三者都一样就是重复了，直接覆盖
        poetryIndex.setAuthor(index.getAuthor());
        poetryIndex.setTitle(index.getTitle());
//        poetryIndex.setContent(index.getContent());
        return SecureUtil.md5(JSON.toJSONString(poetryIndex));
    }

    default void savePoetryToDb(List<PoetryIndex> poetryIndexList){
        List<Poetrys> addPoetryList = poetryIndexList.stream().map(p -> {
            Poetrys poetrys = new Poetrys();
            poetrys.setAuthor(p.getAuthor());
            poetrys.setTitle(p.getTitle());
            poetrys.setContent(JSON.toJSONString(p.getContent()));
            poetrys.setTags(JSON.toJSONString(p.getTags()));
            poetrys.setSid(SecureUtil.md5(String.format("rstyro-%s-%s", poetrys.getAuthor(),poetrys.getTitle())));
            poetrys.setDynasty(JSON.toJSONString(p.getDynasty()));
            return poetrys;
        }).collect(Collectors.toList());
        IPoetrysService poetrysService = BeanContext.getBean(IPoetrysService.class);
        poetrysService.saveOrUpdateBatch(addPoetryList);
    }

}
