package com.express.pickup.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 */

@Data
@TableName("orders")
public class Order implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    
    private Long userId;
    
    private Long deliveryId;
    
    private Long stationId;
    
    private Long specId;
    
    private String pickupCode;
    
    private Long buildingId;
    
    private String roomNumber;
    
    private String remark;
    
    private BigDecimal basePrice;
    
    private BigDecimal extraPrice;
    
    private BigDecimal totalPrice;
    
    private BigDecimal deliveryIncome;
    
    private BigDecimal platformIncome;
    
    private Integer status;
    
    private Integer payStatus;
    
    private LocalDateTime payTime;
    
    private LocalDateTime receiveTime;
    
    private LocalDateTime completeTime;
    
    private LocalDateTime cancelTime;
    
    private String cancelReason;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String deliveryName;
    
    @TableField(exist = false)
    private String stationName;
    
    @TableField(exist = false)
    private String specName;
    
    @TableField(exist = false)
    private String buildingName;
}
