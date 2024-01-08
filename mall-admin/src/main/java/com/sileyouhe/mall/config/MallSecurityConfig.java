package com.sileyouhe.mall.config;

import com.sileyouhe.mall.component.DynamicSecurityService;
import com.sileyouhe.mall.dto.AdminUserDetails;
import com.sileyouhe.mall.mbg.model.UmsResource;
import com.sileyouhe.mall.service.UmsAdminService;
import com.sileyouhe.mall.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
public class MallSecurityConfig{

    @Autowired
    private UmsAdminService adminService;


    @Autowired
    private UmsResourceService resourceService;

    // Spring Security中用户信息的抽象，用于封装成authentication对象
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> {
            UserDetails admin = adminService.loadUserByUsername(username);
            if (admin != null) {
                return admin;
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }


    // 动态权限配置服务
    // 如果spring检测到这个服务被装进容器，那么动态权限配置就会启动
    @Bean
    public DynamicSecurityService dynamicSecurityService(){
        return new DynamicSecurityService() {
            @Override
            public Map<String, ConfigAttribute> loadDataSource() {
                Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
                List<UmsResource> resourceList = resourceService.listAll();
                for (UmsResource resource : resourceList) {
                    map.put(resource.getUrl(), new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
                }
                return map;
            }
        };
    }
}
