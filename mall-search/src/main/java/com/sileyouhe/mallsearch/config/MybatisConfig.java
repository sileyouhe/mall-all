package com.sileyouhe.mallsearch.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.sileyouhe.mallsearch.mbg.mapper","com.sileyouhe.mallsearch.dao"})
public class MybatisConfig {
}
