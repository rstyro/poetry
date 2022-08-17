package top.rstyro.poetry.service.impl;

import cn.hutool.core.bean.BeanUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.rstyro.poetry.dto.SearchDto;
import top.rstyro.poetry.es.base.EsResult;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.es.vo.EsSearchResultVo;
import top.rstyro.poetry.service.IPoetryService;
import top.rstyro.poetry.util.LambdaUtil;
import top.rstyro.poetry.vo.SearchVo;

import java.util.ArrayList;
import java.util.List;

@Service
public class PoetryServiceImpl implements IPoetryService {

    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    @Override
    public EsSearchResultVo<SearchVo> getList(SearchDto dto) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String kw = dto.getKw();
        boolQuery.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getTitle),kw));
        boolQuery.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getContent),kw));
        boolQuery.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getAuthor),kw));
        searchSourceBuilder.query(boolQuery);
        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // * 全部字段
        highlightBuilder.field("*");
        searchSourceBuilder.highlighter(highlightBuilder);
        EsResult<PoetryIndex> response = poetryEsService.search(searchSourceBuilder);
        EsSearchResultVo<SearchVo> resultVo = new EsSearchResultVo<>();
        resultVo.setTook(response.getTook());
        resultVo.setTotal(response.getTotal());
        List<SearchVo> list = new ArrayList<>();
        List<PoetryIndex> records = response.getRecords();
        if(!ObjectUtils.isEmpty(records)){
            records.stream().forEach(i->{
                SearchVo vo = new SearchVo();
                BeanUtil.copyProperties(i,vo);
                list.add(vo);
            });
        }
        resultVo.setRecords(list);
        return resultVo;
    }
}
