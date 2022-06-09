package top.rstyro.poetry;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.rstyro.poetry.es.base.EsResult;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.util.LambdaUtil;

@SpringBootTest
class PoetryApplicationTests {


    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    @Test
    void contextLoads() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.existsQuery(LambdaUtil.getFieldName(PoetryIndex::getTitle)));
        sourceBuilder.trackTotalHits(true).query(boolQuery);
        EsResult<PoetryIndex> search = poetryEsService.search(sourceBuilder);
        System.out.println(search);
    }

}
