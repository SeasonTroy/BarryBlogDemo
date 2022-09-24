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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_notice_user")
@ApiModel(value="NoticeUser对象", description="")
public class NoticeUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "nose_id", type = IdType.AUTO)
    private Integer noseId;

    @ApiModelProperty(value = "通知id")
    private Integer noticeId;

    @TableField(exist = false)
    private Notice notice;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "是否查看")
    private Integer isRead;

    @ApiModelProperty(value = "查看时间")
    private LocalDateTime readtime;


}
