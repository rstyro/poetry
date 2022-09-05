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
import top.rstyro.poetry.process.*;
import top.rstyro.poetry.util.LambdaUtil;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PoetryApplicationTests {


    private PoetryEsService poetryEsService;
    private ChuCiHandler chuCiHandler;
    private TangSongHandler tangSongHandler;
    private CaoCaoShiJiHandler caoCaoShiJiHandler;
    private SiShuHandler siShuHandler;
    private ShiJingHandler shiJingHandler;
    private HuaJianJiHandler huaJianJiHandler;
    private NanTangHandler nanTangHandler;

    @Autowired
    public void setChuCiHandler(ChuCiHandler chuCiHandler) {
        this.chuCiHandler = chuCiHandler;
    }

    @Autowired
    public void setTangSongHandler(TangSongHandler tangSongHandler) {
        this.tangSongHandler = tangSongHandler;
    }

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    @Autowired
    public void setCaoCaoShiJiHandler(CaoCaoShiJiHandler caoCaoShiJiHandler) {
        this.caoCaoShiJiHandler = caoCaoShiJiHandler;
    }

    @Autowired
    public void setSiShuHandler(SiShuHandler siShuHandler) {
        this.siShuHandler = siShuHandler;
    }

    @Autowired
    public void setShiJingHandler(ShiJingHandler shiJingHandler) {
        this.shiJingHandler = shiJingHandler;
    }

    @Autowired
    public void setHuaJianJiHandler(HuaJianJiHandler huaJianJiHandler) {
        this.huaJianJiHandler = huaJianJiHandler;
    }

    @Autowired
    public void setNanTangHandler(NanTangHandler nanTangHandler) {
        this.nanTangHandler = nanTangHandler;
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


//        List<PoetryIndex> list = new ArrayList<>();
//        PoetryIndex poetryIndex = new PoetryIndex();
//        poetryIndex.set_id("b-yVR4EBW0G4uLadnYW1");
//        poetryIndex.setAuthor("屈原2");
//
//        PoetryIndex poetryIndex2 = new PoetryIndex();
//        poetryIndex2.set_id("5CPI1oEBW_zZZa3IQjxb");
//        poetryIndex2.setAuthor("屈原3");
//
//        PoetryIndex poetryIndex3 = new PoetryIndex();
//        poetryIndex3.set_id("b-yVR4EBW0G4uLadnYW2");
//        poetryIndex3.setAuthor("屈原4");
//        list.add(poetryIndex);
//        list.add(poetryIndex2);
//        list.add(poetryIndex3);
//        boolean b = poetryEsService.batchUpdateDoc(list);
//        System.out.println("b="+b);


//        chuCiHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\chuci");
//        tangSongHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\json");
//        caoCaoShiJiHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\caocaoshiji\\caocao.json");
//        tangSongHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\quan_tang_shi\\json");
//        siShuHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\quan_tang_shi\\json");
//        shiJingHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\shijing\\shijing.json");
//        huaJianJiHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\wudai\\huajianji");
        nanTangHandler.handler("D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\wudai\\nantang");


//        tangSongHandler.handler("E:\\古诗检索系统\\chinese-poetry-master\\chinese-poetry-master\\json");
    }

}
