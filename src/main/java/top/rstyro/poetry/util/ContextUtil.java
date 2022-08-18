package top.rstyro.poetry.util;

/**
 * 上下文工具类
 * @author rstyro
 */
public class ContextUtil {
    private final static ThreadLocal<Integer> pageNo = new ThreadLocal<>();
    private final static ThreadLocal<Integer> pageSize = new ThreadLocal<>();


    public static void setPageNo(Integer page) {
        if(page<1)page=1;
        pageNo.set(page);
    }

    public static Integer getPageNo() {
        return pageNo.get()==null?1:pageNo.get();
    }

    public static void setPageSize(Integer size) {
        pageSize.set(size);
    }

    public static Integer getPageSize() {
        return pageSize.get()==null?10:pageSize.get();
    }
}
