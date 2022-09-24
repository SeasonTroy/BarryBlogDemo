package com.bootdang.system.entity;

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
 * 用户下载表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_download")
@ApiModel(value="Download对象", description="用户下载表")
public class Download implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "download_id", type = IdType.AUTO)
    private Integer downloadId;

    @ApiModelProperty(value = "下载时间")
    private LocalDateTime downloadtime;

    @ApiModelProperty(value = "资源id")
    private Integer articleId;

    @ApiModelProperty(value = "下载的用户id")
    private Integer userId;


}
