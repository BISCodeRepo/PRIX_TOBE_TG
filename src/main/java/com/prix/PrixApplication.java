package com.prix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
@EntityScan("com.prix.user")
@EnableJpaRepositories("com.prix.user")
@EnableJpaAuditing
@Component
public class PrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrixApplication.class, args);
    }

    @Bean
    public static HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
    }

//    @Bean
//    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter(){
//        FilterRegistrationBean<HiddenHttpMethodFilter> filterRegistrationBean = new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
//        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 필터 우선순위 설정
//        return filterRegistrationBean;
//    }
}