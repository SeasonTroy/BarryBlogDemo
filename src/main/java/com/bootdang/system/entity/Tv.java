package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("sys_tv")
@ApiModel(value="Tv对象", description="")
public class Tv implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "电影id")
    @TableId(value = "tv_id", type = IdType.AUTO)
    private Integer tvId;

    private Integer typeId;

    @ApiModelProperty(value = "电影名称")
    private String name;

    @ApiModelProperty(value = "电影封面")
    private String imageurl;

    @ApiModelProperty(value = "电影地址")
    private String playurl;

    @ApiModelProperty(value = "播放时长")
    private String duration;

    @ApiModelProperty(value = "电影描述")
    private String description;

    @ApiModelProperty(value = "关注焦点")
    private String focus;

    @ApiModelProperty(value = "点击量")
    private Integer click;

    @ApiModelProperty(value = "上映时间")
    @TableField("formatPeriod")
    private String formatPeriod;

    @ApiModelProperty(value = "作者信息")
    private String guests;

    @ApiModelProperty(value = "观看所需积分")
    private Integer jf;

    @ApiModelProperty(value = "是否免费")
    private Integer isFree;


}
