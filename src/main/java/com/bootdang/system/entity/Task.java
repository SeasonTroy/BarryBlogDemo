package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_task")
@ApiModel(value="Task对象", description="")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "创建的用户id")
    private Integer createuserid;

    @ApiModelProperty(value = "需要执行任务的job全类名")
    private String classmethod;

    @ApiModelProperty(value = "修改的时间")
    private LocalDateTime updatetime;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "job任务的名称")
    private String jobName;

    @ApiModelProperty(value = "任务的分组")
    private String jobGroup;

    @ApiModelProperty(value = "任务的状态")
    private String state;


}
