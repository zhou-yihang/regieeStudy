package com.powerbyte.regiee.config;

import com.powerbyte.regiee.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 配置静态资源访问路径
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始---配置静态资源访问路径");

        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");

        log.info("结束---配置静态资源访问路径");
    }

    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器
        converter.setObjectMapper(new JacksonObjectMapper());
        //设置消息转换器
        converters.add(0,converter);
    }
}
