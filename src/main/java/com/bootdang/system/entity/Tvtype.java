package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tvtype")
@ApiModel(value="Tvtype对象", description="")
@AllArgsConstructor
@NoArgsConstructor
public class Tvtype implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类别id")
    @TableId(value = "tvtype_id",type =IdType.AUTO)
    private Integer tvtypeId;

    @ApiModelProperty(value = "类别标识")
    @TableField("i_dentification")
    private String Identification;

    @ApiModelProperty(value = "类别标题")
    private String title;

    @ApiModelProperty(value = "点击量")
    private Integer click;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "状态")
    private String state;


}
