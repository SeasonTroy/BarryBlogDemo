package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="User对象", description="用户实体类")
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @Size(min = 3,max = 15,message = "用户名必须3到15位")
    @ApiModelProperty(value = "用户名")
    private String username;

    @Size(min = 5,max = 16,message = "密码必须5到16位")
    @ApiModelProperty(value = "用户密码")
    private String password;

    @Size(min = 5,max = 16,message = "密码必须5到16位")
    @TableField(exist = false)
    private String xpassword;

    @Size(min = 2,max = 10,message = "姓名必须2到10")
    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value="是否是vip")
    private Integer isVip;

   @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    @TableField("Birthday")
    private LocalDate Birthday;


    @ApiModelProperty(value = "用户创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "最近修改时间")
    private LocalDateTime updatetime;

    @ApiModelProperty(value = "最后登录")
    private LocalDateTime lastlogin;

    @ApiModelProperty(value = "创建用户")
    private Integer createuserid;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "个人描述")
    private String context;

    @ApiModelProperty(value = "省份")
    @TableField("Province")
    private String Province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "头像")
    private String logo;

    @ApiModelProperty(value = "工作")
    private String work;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色")
    private List<Role> role;

    @ApiModelProperty(value = "部门id")
    private Integer depaid;

    @ApiModelProperty(value = "登录次数")
    private Integer logincount;

    @ApiModelProperty(value = "爱好")
    private String hobby;

    @ApiModelProperty(value = "拥有积分")
    private Integer jf;

    @ApiModelProperty(value = "是否超级管理员")
    private Integer isAdmin;

    @Email(message = "邮箱格式错误")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Size(min = 6,max = 11)
    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "市")
    private String area;


}
