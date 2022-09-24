package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 轮播表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_wheel")
@ApiModel(value="Wheel对象", description="轮播表")
public class Wheel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "wheel_id", type = IdType.AUTO)
    private Integer wheelId;

    @ApiModelProperty(value = "所属栏目")
    private Integer arttypeId;

    @TableField(exist = false)
    private String imageurls;

    @TableField(exist = false)
    private String arttypename;

    private String url;

    @ApiModelProperty(value = "轮播标题")
    private String title;

    @ApiModelProperty(value = "图片地址")
    private String imageurl;

    @ApiModelProperty(value = "创建用户")
    private Integer createuserid;

    @TableField(exist = false)
    private String username;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "排序")
    private Integer num;



}
