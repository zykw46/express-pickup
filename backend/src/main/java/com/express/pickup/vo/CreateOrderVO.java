package com.express.pickup.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderVO {
    
    @NotNull(message = "请选择快递站点")
    private Long stationId;
    
    @NotNull(message = "请选择包裹规格")
    private Long specId;
    
    @NotBlank(message = "请输入取件码")
    private String pickupCode;
    
    @NotNull(message = "请选择宿舍楼")
    private Long buildingId;
    
    @NotBlank(message = "请输入房间号")
    private String roomNumber;
    
    private String remark;
}
