package top.rstyro.poetry.process;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import top.rstyro.poetry.entity.PoetryChuCiVo;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        String filePath = "D:\\my_home\\chinese-poetry-master\\chinese-poetry-master\\chuci";
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.stream(files).forEach(f->{
            FileReader fileReader = new FileReader(f);
            String json = fileReader.readString();
            List<PoetryChuCiVo> poetryChuCiVos = JSON.parseArray(json, PoetryChuCiVo.class);
            System.out.println(poetryChuCiVos.size());
            List<List<PoetryChuCiVo>> split = ListUtil.split(poetryChuCiVos, 10);
            System.out.println(split.get(6));
            System.out.println(split.get(6).size());
        });

    }
}
