package top.rstyro.poetry.es.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AggregationVo<T extends BaseAggregationVo> implements Serializable {
    String key;
    List<T> values;
    long sumDoc;

    public AggregationVo(String key, List<T> values, long sumDoc) {
        this.key = key;
        this.values = values;
        this.sumDoc=sumDoc;
    }

}
