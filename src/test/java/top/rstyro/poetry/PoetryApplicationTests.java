package top.rstyro.poetry;

import lombok.SneakyThrows;
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

    @SneakyThrows
    @Test
    void contextLoads() {
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.must(QueryBuilders.existsQuery(LambdaUtil.getFieldName(PoetryIndex::getTitle)));
//        sourceBuilder.trackTotalHits(true).query(boolQuery);
//        EsResult<PoetryIndex> search = poetryEsService.search(sourceBuilder);
//        System.out.println(search);


//        for (int i = 0; i < 5; i++) {
//            PoetryIndex poetIndex = new PoetryIndex();
//            poetIndex.setTitle("云中君").setSection("九歌").setAuthor("屈原");
//            List<String> content = new ArrayList<>();
//            content.add("浴兰汤兮沐芳，华采衣兮若英");
//            content.add("灵连蜷兮既留，烂昭昭兮未央");
//            content.add("謇将憺兮寿宫，与日月兮齐光");
//            content.add("龙驾兮帝服，聊翱游兮周章");
//            content.add("灵皇皇兮既降，猋远举兮云中");
//            content.add("览冀洲兮有余，横四海兮焉穷");
//            content.add("思夫君兮太息，极劳心兮忡忡");
//            poetIndex.setContent(content);
//            boolean b = poetryEsService.saveDoc(poetIndex);
//            System.out.println(b);
//        }


        List<PoetryIndex> list = new ArrayList<>();
        PoetryIndex poetryIndex = new PoetryIndex();
        poetryIndex.set_id("b-yVR4EBW0G4uLadnYW1");
        poetryIndex.setAuthor("屈原2");

        PoetryIndex poetryIndex2 = new PoetryIndex();
        poetryIndex2.set_id("5CPI1oEBW_zZZa3IQjxb");
        poetryIndex2.setAuthor("屈原3");

        PoetryIndex poetryIndex3 = new PoetryIndex();
        poetryIndex3.set_id("b-yVR4EBW0G4uLadnYW2");
        poetryIndex3.setAuthor("屈原4");
        list.add(poetryIndex);
        list.add(poetryIndex2);
        list.add(poetryIndex3);
        boolean b = poetryEsService.batchUpdateDoc(list);
        System.out.println("b="+b);
    }

}
