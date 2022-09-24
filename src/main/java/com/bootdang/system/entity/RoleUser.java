package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_user")
@ApiModel(value="RoleUser对象", description="")
public class RoleUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer userId;


}
