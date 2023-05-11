package com.sileyouhe.mallsecurity.config;


import com.sileyouhe.mallsecurity.component.JwtAuthenticationTokenFilter;
import com.sileyouhe.mallsecurity.component.RestAuthenticationEntryPoint;
import com.sileyouhe.mallsecurity.component.RestfulAccessDeniedHandler;
import com.sileyouhe.mallsecurity.util.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();

        // urls ignore
        for (String url : ignoreUrlsConfig().getUrls() ) {
            registry.antMatchers(url).permitAll();
        }

        // options permit
        registry.antMatchers(HttpMethod.OPTIONS)
                        .permitAll();

        // authenticated and csrf, session disable
        registry.and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // disable cache
        httpSecurity.headers().cacheControl();

        // add JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // add access denied Handler and return Entry point
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler())
                .authenticationEntryPoint(restAuthenticationEntryPoint());

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig() {
        return new IgnoreUrlsConfig();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler() {
        return new RestfulAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }
}
