package com.sileyouhe.mall.config;


import com.sileyouhe.mall.component.DynamicAccessDecisionManager;
import com.sileyouhe.mall.component.DynamicSecurityFilter;
import com.sileyouhe.mall.component.DynamicSecurityMetadataSource;
import com.sileyouhe.mall.component.JwtAuthenticationTokenFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Security 通用组件
@Configuration
public class CommonSecurityConfig {

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


    // 动态权限管理组件
//    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
        return new DynamicAccessDecisionManager();
    }

    // 动态权限管理组件
//    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource() {
        return new DynamicSecurityMetadataSource();
    }

    // 动态权限管理组件
//    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter(){
        return new DynamicSecurityFilter();
    }
}
