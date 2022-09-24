package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_power")
@ApiModel(value="Power对象", description="")
public class Power implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pow_id", type = IdType.AUTO)
    private Integer powId;

    @ApiModelProperty(value = "权限类别")
    private Integer powerType;

    @ApiModelProperty(value = "类别id比如菜单id按钮id等")
    private Integer menuId;

    @ApiModelProperty(value = "权限标识控制是否允许访问")
    private String perms;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @TableField(exist = false)
    private Menu menu;

    @ApiModelProperty(value = "权限描述")
    private String description;

}
