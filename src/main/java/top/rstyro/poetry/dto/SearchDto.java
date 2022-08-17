package top.rstyro.poetry.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SearchDto {
    /**
     * 检索关键词
     */
    private String kw;
    /**
     * 过滤项
     */
    private SearchFilterDto filters;
    /**
     * 聚类
     */
    private List<SearchAggsDto> aggsList;
    /**
     * 需要查询结果
     */
    boolean needRecords = true;
}
