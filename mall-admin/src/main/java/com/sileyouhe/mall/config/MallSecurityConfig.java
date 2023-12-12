package com.sileyouhe.mall.config;

import com.sileyouhe.mall.dto.AdminUserDetails;
import com.sileyouhe.mall.mbg.model.UmsAdmin;
import com.sileyouhe.mall.mbg.model.UmsPermission;
import com.sileyouhe.mall.mbg.model.UmsResource;
import com.sileyouhe.mall.service.UmsAdminService;
import com.sileyouhe.mall.service.UmsResourceService;
import com.sileyouhe.mallsecurity.component.DynamicSecurityService;
import com.sileyouhe.mallsecurity.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MallSecurityConfig extends SecurityConfig {

    @Autowired
    private UmsAdminService AdminService;

    @Autowired
    private UmsResourceService resourceService;

    @Bean
    public UserDetailsService userDetailsService(){

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UmsAdmin admin = AdminService.getAdminByUsername(username);
                if (admin != null){
                    List<UmsPermission> permissionList = AdminService.getPermissionList(admin.getId());
                    return new AdminUserDetails(admin, permissionList);
                }
                throw new UsernameNotFoundException("error: invalid username or password");
            }
        };
//        return username -> {
//            UmsAdmin admin = AdminService.getAdminByUsername(username);
//            if (admin != null){
//                List<UmsPermission> permissionList = AdminService.getPermissionList(admin.getId());
//                return new AdminUserDetails(admin, permissionList);
//            }
//            throw new UsernameNotFoundException("error: invalid username or password");
//        };
    }

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
