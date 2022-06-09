package top.rstyro.poetry.es.index;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class EsBaseIndex implements Serializable {
    /**
     * es 主键ID
     */
    private String _id;
    /**
     * 命中分数
     */
    private float _score;

    /**
     * 高亮
     */
    Map<String, List<String>> highlight;

    /**
     * es 索引名称
     * @return
     */
    public String getIndexName() {
        return StrUtil.toSymbolCase(StrUtil.removeSuffix(getClass().getSimpleName(), "Index"), '-');
    }



}
