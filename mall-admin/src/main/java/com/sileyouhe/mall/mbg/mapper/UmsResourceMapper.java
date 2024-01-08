package com.sileyouhe.mall.mbg.mapper;

import com.sileyouhe.mall.mbg.model.UmsResource;
import com.sileyouhe.mall.mbg.model.UmsResourceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsResourceMapper {
    long countByExample(UmsResourceExample example);

    int deleteByExample(UmsResourceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsResource row);

    int insertSelective(UmsResource row);

    List<UmsResource> selectByExample(UmsResourceExample example);

    UmsResource selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UmsResource row, @Param("example") UmsResourceExample example);

    int updateByExample(@Param("row") UmsResource row, @Param("example") UmsResourceExample example);

    int updateByPrimaryKeySelective(UmsResource row);

    int updateByPrimaryKey(UmsResource row);

    /**
     * 自定义方法
     * 获取用户所有可访问资源
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 自定义方法
     * 根据角色ID获取资源
     */
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);
}