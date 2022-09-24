package com.bootdang.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_file")
public class FileEntity implements Serializable {

    private static final long serialVersionUID = 1116379022545078439L;

    @TableId(value = "file_id", type = IdType.AUTO)
    private Integer fileId;
    @ApiModelProperty(value = "文件类型")
    private Integer fileType;
    @ApiModelProperty(value = "文件地址")
    private String fileUrl;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;
    @ApiModelProperty(value = "创建的用户")
    private Integer createuserid;
    @ApiModelProperty(value = "状态")
    private Integer state;
}
