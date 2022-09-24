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
@TableName("sys_system")
@ApiModel(value="System对象", description="")
public class System implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "网站id")
    private Integer id;

    @ApiModelProperty(value = "网站标题")
    private String title;

    @ApiModelProperty(value = "网站关键词")
    private String keyword;

    @ApiModelProperty(value = "网站描述")
    private String description;

    @ApiModelProperty(value = "首页励志语")
    private String usertext;

    @ApiModelProperty(value = "网站logo")
    private String logo;

    @ApiModelProperty(value = "微信二维码")
    private String weixin;

    @ApiModelProperty(value = "备案号")
    private String beiantext;

    @ApiModelProperty(value = "网站状态")
    private Integer state;

    @ApiModelProperty(value = "开发者")
    private String developer;

    @ApiModelProperty(value = "版权信息")
    private String powerby;


}
