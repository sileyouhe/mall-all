package com.sileyouhe.mall.config;

import com.sileyouhe.mall.component.*;
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
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class NewSecurityConfig {

    // 组件RestfulAccessDeniedHandler：来自component文件夹
    // 当用户没有访问权限时的处理器，用于返回JSON格式的处理结果；
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    // 组件RestAuthenticationEntryPoint：来自component文件夹
    // 当未登录或token失效时，返回JSON格式的结果；
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    // 组件DynamicSecurityService，来自MallSecurityConfig（可选）
    // 动态权限控制服务
    @Autowired(required = false)
    private DynamicSecurityService dynamicSecurityService;

    @Autowired(required = false)
    private DynamicSecurityFilter dynamicSecurityFilter;

    // 组件IgnoreUrlsConfig，来自config文件夹
    // 进行白名单路径配置，符合的路径不会拦截；
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    // 组件JwtAuthenticationTokenFilter ，提供登录服务的过滤器
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


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
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 增加未授权访问，未登录访问的处理器
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        // 如果检测到了DynamicSecurityService存在于容器里，那么添加动态权限过滤器
        if (dynamicSecurityService != null){
            registry.and().addFilterBefore(dynamicSecurityFilter, FilterSecurityInterceptor.class);
        }

        return httpSecurity.build();
    }


}
