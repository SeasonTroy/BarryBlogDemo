package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_default_adminuser")
@ApiModel(value="DefaultAdminuser对象", description="")
public class DefaultAdminuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户默认头像")
    @TableField("defaultuserLogo")
    private String defaultuserLogo;

    @ApiModelProperty(value = "管理员默认头像")
    private String defaultadminlogo;

    @ApiModelProperty(value = "默认签名")
    @TableField("defaultContext")
    private String defaultContext;


}
