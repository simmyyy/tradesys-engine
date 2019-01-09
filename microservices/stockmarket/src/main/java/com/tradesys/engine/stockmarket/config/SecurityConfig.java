package com.tradesys.engine.stockmarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(100)
/**
 * For testing purposes only
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String[] PERMIT_ALL_ANT_MATCHERS = {
            "/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers(PERMIT_ALL_ANT_MATCHERS).permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf()
                .disable().headers().frameOptions().disable();
    }

}
