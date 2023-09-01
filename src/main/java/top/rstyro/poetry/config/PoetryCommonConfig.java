package top.rstyro.poetry.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Data
@ConfigurationProperties(prefix = "poetry.common")
@Configuration
public class PoetryCommonConfig {

    @Resource
    private TtsConfig ttsConfig;

    @Data
    @ConfigurationProperties(prefix = "poetry.common.tts")
    @Configuration
    public class TtsConfig {
        private String rootPath;
    }
}
