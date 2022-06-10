package top.rstyro.poetry;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.rstyro.poetry.es.base.EsResult;
import top.rstyro.poetry.es.index.PoetIndex;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.util.LambdaUtil;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PoetryApplicationTests {


    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    @Test
    void contextLoads() {
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.must(QueryBuilders.existsQuery(LambdaUtil.getFieldName(PoetryIndex::getTitle)));
//        sourceBuilder.trackTotalHits(true).query(boolQuery);
//        EsResult<PoetryIndex> search = poetryEsService.search(sourceBuilder);
//        System.out.println(search);


        PoetryIndex poetIndex = new PoetryIndex();
        poetIndex.setTitle("云中君").setSection("九歌").setAuthor("屈原");
        List<String> content = new ArrayList<>();
        content.add("浴兰汤兮沐芳，华采衣兮若英");
        content.add("灵连蜷兮既留，烂昭昭兮未央");
        content.add("謇将憺兮寿宫，与日月兮齐光");
        content.add("龙驾兮帝服，聊翱游兮周章");
        content.add("灵皇皇兮既降，猋远举兮云中");
        content.add("览冀洲兮有余，横四海兮焉穷");
        content.add("思夫君兮太息，极劳心兮忡忡");
        poetIndex.setContent(content);
        boolean b = poetryEsService.saveDoc(poetIndex);
        System.out.println(b);
    }

}
