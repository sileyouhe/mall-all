package com.sileyouhe.mallsecurity.config;

import com.sileyouhe.mallsecurity.component.JwtAuthenticationTokenFilter;
import com.sileyouhe.mallsecurity.component.RestAuthenticationEntryPoint;
import com.sileyouhe.mallsecurity.component.RestfulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class NewSecurityConfig {

    // 组件RestfulAccessDeniedHandler：来自component文件夹
    // 当用户没有访问权限时的处理器，用于返回JSON格式的处理结果；
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    // 组件RestAuthenticationEntryPoint：来自component文件夹
    // 当未登录或token失效时，返回JSON格式的结果；
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    // 组件IgnoreUrlsConfig，来自config文件夹
    // 进行白名单路径配置，符合的路径不会拦截；
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 白名单放行
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
                .authorizeRequests();
        // 不需要保护的资源路径允许访问
        for (String url : ignoreUrlsConfig.getUrls()) {
            registry.antMatchers(url).permitAll();
        }

        //允许跨域请求的OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS)
                .permitAll();

        // spring security 安全配置
        // 1. 不需要csrf 2. 不需要session  3.除了被保护的资源路径，其他的资源访问都需要认证
        httpSecurity.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();

        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 增加JWT filter在UsernamePasswordAuthenticationFilter这个filter之前
        // JWT filter的作用是代替UsernamePasswordAuthenticationFilter完成登录或者token认证操作
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // 增加未授权访问，未登录访问的处理器
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);


        return httpSecurity.build();
    }

    // 组件：PasswordEncoder，来源于spring security自带
    // 用于在登录/注册时对密码进行加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 组件：JwtAuthenticationTokenFilter，来源于component
    // 负责处理登录/认证的JWT filter
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}
