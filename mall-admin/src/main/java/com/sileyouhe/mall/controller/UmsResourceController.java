package com.sileyouhe.mall.controller;


import com.sileyouhe.mall.common.api.CommonResult;
import com.sileyouhe.mall.component.DynamicSecurityMetadataSource;
import com.sileyouhe.mall.mbg.model.UmsResource;
import com.sileyouhe.mall.service.UmsResourceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api(tags = "UmsResourceController", description = "后台资源管理")
@RequestMapping("/resource")
public class UmsResourceController {

    @Autowired
    private UmsResourceService resourceService;

    @Autowired
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    public CommonResult create(@RequestBody UmsResource umsResource){
        int count = resourceService.create(umsResource);
        // 每次后台资源发生，我们都要把存储资源的map给清空
        dynamicSecurityMetadataSource.clearDataSource();
        if (count > 0){
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }

    }
}
