package com.tubai.study_html.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("tb_files")
@ApiModel(value="Files对象")
public class Files{

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 创建时间，插入数据时自动填充
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    /**
     * 修改时间，插入、更新数据时自动填充
     */
    @TableField(value = "modified_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifiedTime;
    /**
     * 删除状态：插入数据时自动填充
     */
    @TableField(value = "delete_status", fill = FieldFill.INSERT)
    @TableLogic
    private boolean deleteStatus;

    @ApiModelProperty(value = "文件位置")
    private String filePath;

    @ApiModelProperty(value = "原始文件名")
    private String fileName;

    @ApiModelProperty(value = "文件后缀")
    private String suffix;
}
