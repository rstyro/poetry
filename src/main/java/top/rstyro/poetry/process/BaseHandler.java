package top.rstyro.poetry.process;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import top.rstyro.poetry.es.index.PoetryIndex;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

}
