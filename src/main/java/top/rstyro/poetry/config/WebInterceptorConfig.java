package top.rstyro.poetry.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.rstyro.poetry.interceptor.ContextInterceptor;

@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
    private ContextInterceptor contextInterceptor;

    @Autowired
    public void setContextInterceptor(ContextInterceptor contextInterceptor) {
        this.contextInterceptor = contextInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(contextInterceptor).addPathPatterns("/**");
    }
}
