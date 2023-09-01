package top.rstyro.poetry;

import cn.hutool.core.io.file.FileWriter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.rstyro.poetry.db.entity.Poetrys;
import top.rstyro.poetry.db.service.IPoetrysService;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class ExportPoetryApplicationTests {

    @Resource
    private IPoetrysService poetrysService;

    @SneakyThrows
    @Test
    void contextLoads() {
        List<Poetrys> list = poetrysService.list();
        String rootPath = "D:\\my_home\\chinese-poetry-master\\content";
        int index=1;
        int count=1;
        for (Poetrys poetrys : list) {
            if(index++%1000==0){
                count+=1;
            }
            String path=rootPath+"\\"+count;
            FileWriter fileWriter = new FileWriter(path+"\\"+poetrys.getSid()+".txt");
            fileWriter.write(poetrys.getContent());
            Thread.sleep(1);
        }

    }

}
