package com.express.pickup.vo;

import lombok.Data;

@Data
public class UserUpdateVO {
    
    private Long id;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private String password;
    private Long roleId;
    private Integer status;
}
