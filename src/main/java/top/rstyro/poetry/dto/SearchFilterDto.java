package top.rstyro.poetry.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchFilterDto {
    private String key;
    private Integer size=10;
}
