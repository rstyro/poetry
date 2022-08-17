package top.rstyro.poetry.util;

import com.spreada.utils.chinese.ZHConverter;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

public class Tools {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "王之好樂甚，則齊其庶幾乎！今之樂猶古之樂也。";
        System.out.println("s="+s);
        String convert = ZHConverter.convert(s, 0);
        System.out.println(convert);
    }

    public static String bigToCn(String content) throws UnsupportedEncodingException {
        if(StringUtils.isEmpty(content)){
            return content;
        }
        return new String(content.getBytes("big5"),"gbk");
    }
}
