package com.example.transactions.config;

import com.example.transactions.config.filter.JwtFilter;
import com.example.transactions.config.filter.JwtFilterImpl;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.Arrays;

@Configuration
@Import({JwtFilterImpl.class})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    public SecurityConfig(
            JwtFilter jwtFilter
    ) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2MessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(
                Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        return converter;
    }

    @Bean
    public FilterRegistrationBean jwtRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(jwtFilter);
        registration.setEnabled(false);
        return registration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilterBefore(jwtFilter, FilterSecurityInterceptor.class)
                .authorizeRequests()
                .antMatchers("/configuration/ui",
                        "/swagger-resources",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger-resources/configuration/ui",
                        "/swagger-ui.html",
                        "/swagger-resources/configuration/security").permitAll()
                .anyRequest().authenticated();


    }
}
