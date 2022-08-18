package top.rstyro.poetry.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SearchFilterDto {
    private List<String> tags;
    private List<String> dynasty;
    private List<String> type;
}
