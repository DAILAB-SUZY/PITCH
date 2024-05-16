package org.cosmic.backend.globals.configs;

import org.cosmic.backend.domain.auth.applications.ServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<ServletFilter> servletFilter() {
        FilterRegistrationBean<ServletFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ServletFilter());
        registrationBean.addUrlPatterns("/todo");  // 이 필터가 적용될 URL 패턴
        return registrationBean;
    }
}