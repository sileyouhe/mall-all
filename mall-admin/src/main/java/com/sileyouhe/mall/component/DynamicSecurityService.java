package com.sileyouhe.mall.component;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

public interface DynamicSecurityService {

    // 从数据库里取出"资源"列表，然后以map的形式在应用里储存起来
    Map<String, ConfigAttribute> loadDataSource();
}
