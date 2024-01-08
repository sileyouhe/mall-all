package com.sileyouhe.mall.component;

import cn.hutool.core.collection.CollUtil;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

public class DynamicAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        // if we did not configure any resources for this URL, just allow
        if (CollUtil.isEmpty(configAttributes)){
            return;
        }

        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while (iterator.hasNext()){
            ConfigAttribute configAttribute = iterator.next();
            // compare the resources the user has with the resources the url needs

            String needAuthority = configAttribute.getAttribute();
            System.out.println("当前需要权限：" + needAuthority);
            System.out.println("当前拥有权限：" + authentication.getAuthorities());
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (needAuthority.trim().equals(grantedAuthority.getAuthority())){
                    return;
                }
            }
        }
        throw new AccessDeniedException("Sorry, you cannot access this URL From DynamicAccessDecisionManager.decide");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
