package com.youlai.auth.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * 安全配置
 * 主要是配置请求访问权限、定义认证管理器、密码加密配置
 * @author:GSHG
 * @date: 2021-08-27 14:14
 * description:
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        //配置请求访问权限
        httpSecurity.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                // @link https://gitee.com/xiaoym/knife4j/issues/I1Q5X6 (接口文档knife4j需要放行的规则)
                .antMatchers("/webjars/**","/doc.html","/swagger-resources/**","/v2/api-docs").permitAll()
                // 其余所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                // 由于使用的是JWT，我们这里不需要csrf,这里禁用csrf
                .and().csrf().disable();
    }


    @Bean
    public AuthenticationManager authenticationManagerBean () throws Exception{
        return super.authenticationManagerBean();
    }


}
