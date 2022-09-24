package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value="Menu对象", description="")
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    @NotEmpty
    @ApiModelProperty(value = "标题")
    private String title;

    @NotNull
    @ApiModelProperty(value = "父栏目id")
    private Integer parentid;


    @ApiModelProperty(value = "创建用户id")
    private Integer createuserid;

    @TableField(exist = false)
    @ApiModelProperty(value = "创建用户")
    private User user;

    @ApiModelProperty(value = "icon小图片")
    private String icon;


    @ApiModelProperty(value = "栏目排序")
    private Integer orderNum;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatetime;

    @NotNull
    @ApiModelProperty(value = "栏目状态")
    private Integer state;

    @NotNull
    @ApiModelProperty(value = "栏目url")
    private String url;


}
