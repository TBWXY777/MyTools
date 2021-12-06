package com.tubai.study_html.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class BaseUUIDLogicModel implements Serializable {
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

}
