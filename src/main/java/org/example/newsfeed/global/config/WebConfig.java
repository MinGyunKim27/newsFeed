package org.example.newsfeed.global.config;

import jakarta.servlet.Filter;
import org.example.newsfeed.global.filter.CustomFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean customFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        // Filter 등록
        filterRegistrationBean.setFilter(new CustomFilter());
        // Filter 순서 설정
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 요청 경로 /images/** → 실제 파일 위치 C:/upload/images/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/upload/images/");
    }
}
