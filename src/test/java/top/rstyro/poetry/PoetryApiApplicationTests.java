package top.rstyro.poetry;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.rstyro.poetry.dto.SearchDto;
import top.rstyro.poetry.es.vo.EsSearchResultVo;
import top.rstyro.poetry.service.IPoetryService;
import top.rstyro.poetry.vo.SearchVo;

@SpringBootTest
class PoetryApiApplicationTests {


    private IPoetryService poetryService;

    @Autowired
    public void setPoetryService(IPoetryService poetryService) {
        this.poetryService = poetryService;
    }

    @SneakyThrows
    @Test
    void contextLoads() {
        EsSearchResultVo<SearchVo> list = poetryService.getList(new SearchDto().setKw("李白"));
        System.out.println(JSON.toJSONString(list));
    }

}
