package com.express.pickup.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 配送认证表
 */

@Data
@TableName("delivery_certification")
public class DeliveryCertification implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String realName;
    
    private String idCard;
    
    private String studentId;
    
    private String phone;
    
    private String idCardFront;
    
    private String idCardBack;
    
    private String studentCard;
    
    private Integer status;
    
    private String rejectReason;
    
    private LocalDateTime submitTime;
    
    private LocalDateTime auditTime;
    
    private Long auditBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String nickname;
}
