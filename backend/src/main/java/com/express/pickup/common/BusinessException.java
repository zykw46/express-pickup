package com.express.pickup.common;

import lombok.Getter;

/**
 * 自定义业务异常
 */

@Getter
public class BusinessException extends RuntimeException {

    /**
     * 业务错误码 code
     * 5xx：服务端业务逻辑错误（默认值）
     * 4xx：客户端请求错误
     */
    private Integer code;

    /**
     * 创建带错误消息的业务异常
     * @param message 错误描述信息（应使用用户可读的友好提示）
     * @implNote 默认错误码设为500（服务端业务错误）
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造函数：创建带错误码和错误消息的业务异常
     * @param code    错误码
     * @param message 错误描述信息（应使用用户可读的友好提示）
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }


    /**
     * 构造函数：创建带错误消息和 cause 业务异常
     * @param message 错误描述信息（应使用用户可读的友好提示）
     * @param cause   异常 cause
     * @implNote 默认错误码设为500（服务端业务错误）
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }
}
