package top.rstyro.poetry.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.spreada.utils.chinese.ZHConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tools {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        String s = "豳风";
//        System.out.println("s="+s);
//        String convert = ZHConverter.convert(s, 1);
//        System.out.println(convert);
        String id="1";
        String name="苏轼";
//        String request = getRequest("https://baike.baidu.com/item/" + name);
        String request = getRequest("https://baike.baidu.com/item/%E8%8B%8F%E8%BD%BC/53906?fromModule=lemma_search-box");
        System.out.println("request="+request);

    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRequest(String url){
        System.out.println("url="+url);
        return HttpRequest.get(url)
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.79")
//                .setHttpProxy("10.168.2.58", 808)
                .enableDefaultCookie().keepAlive(true)
                .execute().body();
    }

    /**
     * 转简体中文
     * @param content 繁体内容
     * @return 简体内容
     */
    public static String cnToSimple(String content){
        if(StringUtils.isEmpty(content)){
            return content;
        }
        return ZHConverter.convert(content,1);
    }

    /**
     * 转简体中文
     */
    public static List<String> cnToSimple(Collection<String> list){
        if(ObjectUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return list.stream().map(Tools::cnToSimple).collect(Collectors.toList());
    }


}
