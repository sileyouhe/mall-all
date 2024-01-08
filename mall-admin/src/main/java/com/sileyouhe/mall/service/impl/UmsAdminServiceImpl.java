package com.sileyouhe.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.sileyouhe.mall.dao.UmsAdminRoleRelationDao;
import com.sileyouhe.mall.dto.AdminUserDetails;
import com.sileyouhe.mall.mbg.mapper.UmsAdminMapper;
import com.sileyouhe.mall.mbg.model.UmsAdmin;
import com.sileyouhe.mall.mbg.model.UmsAdminExample;
import com.sileyouhe.mall.mbg.model.UmsPermission;
import com.sileyouhe.mall.mbg.model.UmsResource;
import com.sileyouhe.mall.service.UmsAdminService;
import com.sileyouhe.mall.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    /**
     * 存放默认用户信息
     */
    private List<AdminUserDetails> adminUserDetailsList = new ArrayList<>();
    /**
     * 存放默认资源信息
     */
    private List<UmsResource> resourceList = new ArrayList<>();

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;


    // 由于我们还没有实装动态权限管理，因此暂时把用户信息直接写在代码里
    @PostConstruct
    private void init(){
        adminUserDetailsList.add(AdminUserDetails.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .authorityList(CollUtil.toList("brand:create","brand:update","brand:delete","brand:list","brand:listAll"))
                .build());
        adminUserDetailsList.add(AdminUserDetails.builder()
                .username("macro")
                .password(passwordEncoder.encode("123456"))
                .authorityList(CollUtil.toList("brand:listAll"))
                .build());
        resourceList.add(UmsResource.builder()
                .id(1L)
                .name("brand:create")
                .url("/brand/create")
                .build());
        resourceList.add(UmsResource.builder()
                .id(2L)
                .name("brand:update")
                .url("/brand/update/**")
                .build());
        resourceList.add(UmsResource.builder()
                .id(3L)
                .name("brand:delete")
                .url("/brand/delete/**")
                .build());
        resourceList.add(UmsResource.builder()
                .id(4L)
                .name("brand:list")
                .url("/brand/list")
                .build());
        resourceList.add(UmsResource.builder()
                .id(5L)
                .name("brand:listAll")
                .url("/brand/listAll")
                .build());
    }
    @Override
    public AdminUserDetails getAdminByUsername(String username) {
//        UmsAdminExample example = new UmsAdminExample();
//        example.createCriteria().andUsernameEqualTo(username);
//        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
//        if (adminList != null && adminList.size() > 0){
//            return adminList.get(0);
//        }
//        return null;
        List<AdminUserDetails> findList = adminUserDetailsList.stream().filter(item -> item.getUsername().equals(username)).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(findList)){
            return findList.get(0);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdmin umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam,umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        // check if same username in database
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList.size() > 0){
            return null;
        }
        // encode password
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = getAdminByUsername(username);
            if (!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("incorrect password");
            }
            // trusted user
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);

        } catch (AuthenticationException e){
            LOGGER.warn("login error:{}",e.getMessage());
        }
        return token;
    }

    @Override
    public List<UmsPermission> getPermissionList(Long adminId) {

        System.out.println("getPermission user id:" + adminId);
        List<UmsPermission> permissionList = adminRoleRelationDao.getPermissionList(adminId);
        System.out.println("user permission" + permissionList.toString());
        return permissionList;
    }


}
