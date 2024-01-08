package com.sileyouhe.mall.service;


import com.sileyouhe.mall.dto.AdminUserDetails;
import com.sileyouhe.mall.mbg.model.UmsAdmin;
import com.sileyouhe.mall.mbg.model.UmsPermission;
import com.sileyouhe.mall.mbg.model.UmsResource;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UmsAdminService {

    /**
     * get Admin by username
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * register
     */
    UmsAdmin register(UmsAdmin umsAdminParam);

    /**
     * log in
     * @param username
     * @param password
     * @return generated JWT TOKEN
     */
    String login(String username, String password);

    /**
     * get user's permission list
     *
     */
    List<UmsPermission> getPermissionList(Long adminId);


    /**
     * 去数据库获取用户信息
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 获取指定用户的可访问资源
     */
    List<UmsResource> getResourceList(Long adminId);

}
