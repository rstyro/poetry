package top.rstyro.poetry.service;

import top.rstyro.poetry.dto.SearchDto;
import top.rstyro.poetry.es.vo.EsSearchResultVo;
import top.rstyro.poetry.vo.SearchVo;

public interface IPoetryService {
    public EsSearchResultVo<SearchVo> getList(SearchDto dto);
}
