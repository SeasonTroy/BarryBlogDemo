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

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_article")
@ApiModel(value="Article对象", description="资源表")
public class Article {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ar_id", type = IdType.AUTO)
    private Integer arId;

    @ApiModelProperty(value = "类别id")
    private Integer arttypeId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图片地址")
    private String litpic;

    @ApiModelProperty(value = "文章内容")
    private String context;

    @ApiModelProperty(value = "文章描述")
    private String description;

    @ApiModelProperty(value = "创建人id")
    private Integer createuserid;


    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatetime;

    @ApiModelProperty(value = "评论数量")
    private Integer commentcount;

    @ApiModelProperty(value = "是否允许评论")
    private Integer commentState;

    @ApiModelProperty(value = "点击量")
    private Integer clickcount;

    @ApiModelProperty(value = "文章权重")
    private Integer weight;

    @ApiModelProperty(value = "下载地址")
    private String downloadurl;

    @ApiModelProperty(value = "下载密码")
    private String downloadpassword;

    @ApiModelProperty(value = "文章状态1,是通过,2不通过,3,驳回")
    private Integer state;

    @ApiModelProperty(value = "是否顶置")
    private Integer topstate;

    @ApiModelProperty(value = "是否免费")
    private Integer isFree;

    @ApiModelProperty(value = "下载所需积分")
    private Integer jf;

    @ApiModelProperty(value = "是否热门")
    private Integer isHot;

    @ApiModelProperty(value = "驳回内容")
    private String resson;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime checkDatetime;

    @ApiModelProperty(value = "1是文章，二是下载资源，3是资源加文章")
    private Integer type;

    @TableField(exist = false)
    @ApiModelProperty(value = "资源栏目")
    private Arttype arttype;

    @TableField(exist = false)
    @ApiModelProperty(value = "文章用户")
    private User user;

    @TableField(exist = false)
    @ApiModelProperty(value = "文章标签")
    private String[] tag;

    @TableField(exist = false)
    private Integer pingluncount;
}
