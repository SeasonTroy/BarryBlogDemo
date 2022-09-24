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
 * 资源类型表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_arttype")
@ApiModel(value="Arttype对象", description="资源类型表")
public class Arttype implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "acttype_id", type = IdType.AUTO)
    private Integer acttypeId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "栏目url")
    private String url;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "类型描述")
    private String description;

    @ApiModelProperty(value = "状态0.1")
    private Integer state;

    @ApiModelProperty(value = "点击量")
    private Integer click;

    @ApiModelProperty(value = "拼音")
    private String pinyin;

    @ApiModelProperty(value = "父类id")
    private Integer parentid;

    @TableField(exist = false)
    @ApiModelProperty(value = "父栏目名称")
    private String ParentTitle;


}
