package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.rstyro.poetry.db.entity.Author;
import top.rstyro.poetry.db.service.IAuthorService;
import top.rstyro.poetry.entity.PoetryAuthorVo;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 作者提取
 */
@Slf4j
@Component
public class AuthorHandler implements BaseHandler {


    @Resource
    private IAuthorService authorService;

    public void handler(String filePath) {
        try {
            File f = new File(filePath);
            FileReader fileReader = new FileReader(f);
            String json = fileReader.readString();
            List<PoetryAuthorVo> list = JSON.parseArray(json, PoetryAuthorVo.class);
            List<List<PoetryAuthorVo>> splitList = ListUtil.split(list, 1000);
            String dynasty;
            if(f.getName().contains("song")){
                dynasty = "宋朝";
            } else {
                dynasty = "唐朝";
            }
            CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
            splitList.stream().forEach(split -> {
                if (executorService.getActiveCount() < core) {
                    executorService.execute(() -> {
                        batchSavePoetry(split, dynasty, countDownLatch);
                    });
                } else {
                    batchSavePoetry(split, dynasty, countDownLatch);
                }
            });
            countDownLatch.await();
            log.info("处理完文件={}", f.getName());
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }

    public void batchSavePoetry(List<PoetryAuthorVo> list, String dynasty, CountDownLatch countDownLatch) {
        List<Author> dataList = new ArrayList<>();
        try {
            list.forEach(d -> {
                if(!ObjectUtils.isEmpty(d.getName())){
                    Author author = new Author();
                    author.setId(SecureUtil.md5(String.format("rstyro-%s", d.getName())));
                    author.setName(d.getName());
                    author.setDynasty(dynasty);
                    author.setRemark(d.getDesc());
                    dataList.add(author);
                }
            });
            authorService.saveOrUpdateBatch(dataList);
        } catch (Exception e) {
            String key="Duplicate entry '";
            if(e.getMessage().contains(key)){
                String id = e.getMessage().substring(e.getMessage().indexOf(key) + key.length(), e.getMessage().indexOf(key) + key.length() + 32);
                log.error("key={}",dataList.stream().filter(i->i.getId().equals(id)).collect(Collectors.toList()).toString());
            }
            log.error("保存作者报错，err={}", e.getMessage(), e);
        } finally {
            countDownLatch.countDown();
        }
    }

}
