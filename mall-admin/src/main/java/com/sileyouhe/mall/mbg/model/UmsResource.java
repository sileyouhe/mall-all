package com.sileyouhe.mall.mbg.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
@ApiModel(value="UmsResource对象", description="后台资源表")
@Builder
public class UmsResource implements Serializable {
    private Long id;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="资源名称")
    private String name;

    @ApiModelProperty(value="资源URL")
    private String url;

    @ApiModelProperty(value="描述")
    private String description;

    @ApiModelProperty(value="资源分类ID")
    private Long categoryId;
}