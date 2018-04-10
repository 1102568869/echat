package tech.washmore.easychat.common.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import tech.washmore.easychat.common.interceptor.UserLoginInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Washmore
 * @version V1.0
 * @summary MVC相关配置
 * @Copyright (c) 2018, washmore.tech All Rights Reserved.
 * @since 2018/1/15
 */
@Configuration
@ConditionalOnWebApplication
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Value("${spring.profiles.active:}")
    private String env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (env.equals("development")) {
            registry.addMapping("/**")
                    .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowCredentials(true).maxAge(3600);
        }
    }

    @Bean
    public FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4() {
        return new FastJsonHttpMessageConverter4();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation is empty.
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(fastJsonHttpMessageConverter4());
    }


    /**
     * @summary 静态资源处理器
     * @version V1.0
     * @author Washmore
     * @since 2018/1/15
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        CacheControl cacheControl = CacheControl.maxAge(8, TimeUnit.HOURS);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/pages/").setCacheControl(cacheControl);
    }

    /**
     * @summary 拦截器管理
     * @version V1.0
     * @author Washmore
     * @since 2018/1/15
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns("/static/**", "/", "/login", "/verifyToken");
        super.addInterceptors(registry);
    }

    @Bean
    public UserLoginInterceptor loginInterceptor() {
        return new UserLoginInterceptor();
    }

    /**
     * @return
     * @doc http://docs.spring.io/spring-boot/docs/1.5.4.RELEASE/reference/htmlsingle/#boot-features-websockets
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
