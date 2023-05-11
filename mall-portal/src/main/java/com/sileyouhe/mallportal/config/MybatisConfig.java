package com.sileyouhe.mallportal.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.sileyouhe.mallportal.mbg.mapper","com.sileyouhe.mallportal.dao"})
public class MybatisConfig {
}
