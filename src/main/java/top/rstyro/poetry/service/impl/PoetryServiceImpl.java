package top.rstyro.poetry.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.rstyro.poetry.commons.ApiException;
import top.rstyro.poetry.commons.ApiExceptionCode;
import top.rstyro.poetry.commons.Const;
import top.rstyro.poetry.dto.SearchAggsDto;
import top.rstyro.poetry.dto.SearchDto;
import top.rstyro.poetry.dto.SearchFilterDto;
import top.rstyro.poetry.enums.SuggestTypeEnum;
import top.rstyro.poetry.es.base.EsResult;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.es.vo.AggregationVo;
import top.rstyro.poetry.es.vo.EsSearchResultVo;
import top.rstyro.poetry.es.vo.TermAggregationVo;
import top.rstyro.poetry.service.IPoetryService;
import top.rstyro.poetry.util.ContextUtil;
import top.rstyro.poetry.util.LambdaUtil;
import top.rstyro.poetry.vo.SearchDetailVo;
import top.rstyro.poetry.vo.SearchVo;
import top.rstyro.poetry.vo.SuggestVo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class PoetryServiceImpl implements IPoetryService {

    @Value("${spring.profiles.active}")
    private String env;


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
        if (StringUtils.hasLength(kw)) {
            BoolQueryBuilder keywordBool = QueryBuilders.boolQuery();
            keywordBool.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getTitle), kw));
            keywordBool.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getContent), kw));
            keywordBool.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getAuthor), kw));
            boolQuery.must(keywordBool);
        }
        // 过滤项
        if (!ObjectUtils.isEmpty(dto.getFilters())) {
            SearchFilterDto filters = dto.getFilters();
            if (!ObjectUtils.isEmpty(filters.getTags())) {
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getTags), filters.getTags()));
                boolQuery.must(filterBool);
            }
            if (!ObjectUtils.isEmpty(filters.getDynastyList())) {
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getDynasty), filters.getDynastyList()));
                boolQuery.must(filterBool);
            }
            if (!ObjectUtils.isEmpty(filters.getTypeList())) {
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getType), filters.getTypeList()));
                boolQuery.must(filterBool);
            }

            if (!ObjectUtils.isEmpty(filters.getAuthorList())) {
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getAuthor) + ".keyword", filters.getAuthorList()));
                boolQuery.must(filterBool);
            }
        }
        searchSourceBuilder.query(boolQuery);
        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // * 全部字段
        highlightBuilder.field(LambdaUtil.getFieldName(PoetryIndex::getTitle));
        highlightBuilder.field(LambdaUtil.getFieldName(PoetryIndex::getAuthor));
        highlightBuilder.field(LambdaUtil.getFieldName(PoetryIndex::getContent));
        highlightBuilder.fragmentSize(200);
        searchSourceBuilder.highlighter(highlightBuilder);
        // 聚类
        if (!ObjectUtils.isEmpty(dto.getAggsList())) {
            List<SearchAggsDto> aggsList = dto.getAggsList();
            aggsList.stream().forEach(aggs -> {
                searchSourceBuilder.aggregation(AggregationBuilders.terms(aggs.getKey()).field(aggs.getKey()).size(aggs.getSize()).shardSize(1000));
            });
        }
        // 是否需要结果集
        if (dto.getNeedRecords()) {
            int from = (ContextUtil.getPageNo() - 1) * ContextUtil.getPageSize();
            if (from > Const.MAX_RESULT || (from + ContextUtil.getPageSize()) > Const.MAX_RESULT) {
                throw new ApiException(ApiExceptionCode.ES_OVER_MAX_RESULT);
            }
            searchSourceBuilder.from(from).size(ContextUtil.getPageSize());
            searchSourceBuilder.trackTotalHits(true);
//            searchSourceBuilder.
        } else {
            searchSourceBuilder.size(0);
        }
        if ("dev".equals(env)) {
            log.info("ES-SQL={}", searchSourceBuilder.toString());
        }
        EsResult<PoetryIndex> response = poetryEsService.search(searchSourceBuilder);
        EsSearchResultVo<SearchVo> resultVo = new EsSearchResultVo<>();
        resultVo.setTook(response.getTook());
        resultVo.setTotal(response.getTotal());
        List<SearchVo> list = new ArrayList<>();
        List<PoetryIndex> records = response.getRecords();
        if (!ObjectUtils.isEmpty(records)) {
            records.stream().forEach(i -> {
                SearchVo vo = new SearchVo();
                BeanUtil.copyProperties(i, vo);
                list.add(vo);
            });
        }
        resultVo.setRecords(list);
        // 聚类解析
        Aggregations aggregation = response.getAggregation();
        if (!ObjectUtils.isEmpty(aggregation)) {
            dto.getAggsList().stream().forEach(a -> {
                ParsedTerms parsedTerms = aggregation.get(a.getKey());
                if (!ObjectUtils.isEmpty(parsedTerms)) {
                    List<TermAggregationVo> aggregationVoList = new ArrayList<>();
                    AtomicLong sum = new AtomicLong(0);
                    parsedTerms.getBuckets().forEach(bucket -> {
                        TermAggregationVo termAggregationVo = new TermAggregationVo();
                        termAggregationVo.setKey(bucket.getKeyAsString());
                        termAggregationVo.setDocCount(bucket.getDocCount());
                        sum.addAndGet(bucket.getDocCount());
                        aggregationVoList.add(termAggregationVo);
                    });
                    resultVo.addAggregation(new AggregationVo<TermAggregationVo>().setKey(a.getKey()).setList(aggregationVoList).setSumDoc(sum.get()));
                }

            });
        }
        return resultVo;
    }

    @SneakyThrows
    @Override
    public SearchDetailVo getDetail(String id) {
        SearchDetailVo vo =new SearchDetailVo();
        PoetryIndex docById = poetryEsService.getDocById(id);
        BeanUtil.copyProperties(docById,vo);
        return vo;
    }

    @Override
    public List<SuggestVo> getSuggest(String kw) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        String auSuggestFileName = LambdaUtil.getFieldName(PoetryIndex::getAuthor) + ".suggest";
        String titleFileName = LambdaUtil.getFieldName(PoetryIndex::getTitle) + ".suggest";
        String contentFileName = LambdaUtil.getFieldName(PoetryIndex::getContent) + ".suggest";
        suggestBuilder.addSuggestion(auSuggestFileName, getSuggestBuilder(auSuggestFileName, kw));
        suggestBuilder.addSuggestion(titleFileName, getSuggestBuilder(titleFileName, kw));
        suggestBuilder.addSuggestion(contentFileName, getSuggestBuilder(contentFileName, kw));
        searchSourceBuilder.suggest(suggestBuilder);
        searchSourceBuilder.size(0);
        if ("dev".equals(env)) {
            log.info("Suggest-SQL={}", searchSourceBuilder.toString());
        }
        EsResult<PoetryIndex> search = poetryEsService.search(searchSourceBuilder);
        List<SuggestVo> suggestVoList = new ArrayList<>();
        Suggest suggest = search.getSuggest();
        addSuggest(suggest,auSuggestFileName,suggestVoList,SuggestTypeEnum.au);
        addSuggest(suggest,titleFileName,suggestVoList,SuggestTypeEnum.ti);
        addSuggest(suggest,contentFileName,suggestVoList,SuggestTypeEnum.content);
        return suggestVoList;
    }

    public SuggestionBuilder<CompletionSuggestionBuilder> getSuggestBuilder(String fileName, String text) {
        return SuggestBuilders
                .completionSuggestion(fileName)
                .text(text)
                .skipDuplicates(true).size(10);
    }

    public void addSuggest(Suggest suggest, String name, List<SuggestVo> suggestVoList, SuggestTypeEnum typeEnum) {
        Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestion = suggest.getSuggestion(name);
        if (!ObjectUtils.isEmpty(suggestion)) {
            suggestion.getEntries().forEach(i -> {
                if (i.getOptions().size() > 0) {
                    i.getOptions().forEach(o -> {
                        Text text = o.getText();
                        if(suggestVoList.size()<20){
                            suggestVoList.add(new SuggestVo()
                                    .setType(typeEnum.name())
                                    .setText(text.string()));
                        }
                    });
                }
            });
        }
    }
}
