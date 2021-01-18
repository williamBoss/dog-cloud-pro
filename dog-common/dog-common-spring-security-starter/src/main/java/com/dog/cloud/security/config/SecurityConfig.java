package com.dog.cloud.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * security 配置类
 *
 * @author KING
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 权限配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests()
            // 标识只能在 服务器本地ip[127.0.0.1或localhost] 访问`/home`接口，其他ip地址无法访问
            .antMatchers("/home").hasIpAddress("127.0.0.1")
            // 允许匿名的url - 可理解为放行接口 - 多个接口使用,分割
            .antMatchers("/login", "/index").permitAll()
            // OPTIONS(选项)：查找适用于一个特定网址资源的通讯选择。 在不需执行具体的涉及数据传输的动作情况下，
            // 允许客户端来确定与资源相关的选项以及 / 或者要求， 或是一个服务器的性能
            .antMatchers(HttpMethod.OPTIONS, "/**").denyAll()
            // 自动登录 - cookie储存方式
            // .and().rememberMe()
            // 其余所有请求都需要认证
            .anyRequest().authenticated()
            // 防止iframe 造成跨域
            .and().headers().frameOptions().disable()
            // 禁用CSRF 开启跨域
            .and().csrf().disable().cors();
    }

    /**
     * BCrypt密码编码
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
