package top.rstyro.poetry;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.process.*;

import javax.annotation.Resource;

@SpringBootTest
class PoetryAuthorApplicationTests {


    @Resource
    private AuthorHandler authorHandler;


    @SneakyThrows
    @Test
    void contextLoads() {
        authorHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\author\\authors.song.json");
        authorHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\author\\authors.tang.json");
    }

}
