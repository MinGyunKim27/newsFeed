package org.example.newsfeed.global.config;

import jakarta.servlet.Filter;
import org.example.newsfeed.global.filter.CustomFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web MVC 설정 클래스입니다.
 * 정적 리소스 매핑, 커스텀 필터 등록, 비밀번호 인코더 Bean 설정 등의 기능을 제공합니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 커스텀 비밀번호 인코더 Bean을 등록합니다.
     * 실제 보안 인코더가 아닌 경우에는 PasswordEncoder 클래스 구현체를 사용하는 것이 좋습니다.
     *
     * @return PasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder(); // 실제 환경에서는 BCryptPasswordEncoder 등을 사용하는 것이 안전합니다.
    }

    /**
     * CustomFilter를 필터 체인에 등록하는 Bean 설정입니다.
     *
     * @return 필터 등록 설정 객체
     */
    @Bean
    public FilterRegistrationBean customFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        // 커스텀 필터 설정
        filterRegistrationBean.setFilter(new CustomFilter());

        // 필터의 순서를 설정 (값이 낮을수록 먼저 실행됨)
        filterRegistrationBean.setOrder(1);

        // 필터가 적용될 URL 패턴 설정 (전체 요청에 적용)
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
