package top.rstyro.poetry.service.impl;

import cn.hutool.core.bean.BeanUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.rstyro.poetry.dto.SearchAggsDto;
import top.rstyro.poetry.dto.SearchDto;
import top.rstyro.poetry.dto.SearchFilterDto;
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
        // 过滤项
        if(!ObjectUtils.isEmpty(dto.getFilters())){
            SearchFilterDto filters = dto.getFilters();
            if(!ObjectUtils.isEmpty(filters.getTags())){
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getTags),filters.getTags()));
                boolQuery.must(filterBool);
            }
            if(!ObjectUtils.isEmpty(filters.getDynasty())){
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getDynasty),filters.getDynasty()));
                boolQuery.must(filterBool);
            }
            if(!ObjectUtils.isEmpty(filters.getType())){
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getType),filters.getType()));
                boolQuery.must(filterBool);
            }
        }
        searchSourceBuilder.query(boolQuery);
        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // * 全部字段
        highlightBuilder.field("*");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 聚类
        if(!ObjectUtils.isEmpty(dto.getAggsList())){
            List<SearchAggsDto> aggsList = dto.getAggsList();
            aggsList.stream().forEach(aggs->{
                searchSourceBuilder.aggregation(AggregationBuilders.terms(aggs.getKey()).field(aggs.getKey()).size(aggs.getSize()).shardSize(1000));
            });
        }
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
