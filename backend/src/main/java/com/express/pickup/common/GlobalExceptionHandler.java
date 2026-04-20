package com.express.pickup.common;

import cn.dev33.satoken.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */

@Slf4j   //@Slf4j注解是Lombok库提供的一个便捷工具，用于在Java类中自动生成SLF4J日志记录器
@RestControllerAdvice  //spring框架提供的一个核心注解，实现RESTful API的全局异常处理和统一响应格式
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 登录异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    public Result<Void> handleNotLoginException(NotLoginException e) {
        log.warn("未登录: {}", e.getMessage());
        return Result.error(401, "请先登录");
    }

    /**
     * 无角色权限处理
     * @param e
     * @return
     */
    @ExceptionHandler(NotRoleException.class)
    public Result<Void> handleNotRoleException(NotRoleException e) {
        log.warn("无角色权限: {}", e.getMessage());
        return Result.error(403, "无权限访问");
    }

    /**
     * 无权限处理
     * @param e
     * @return
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<Void> handleNotPermissionException(NotPermissionException e) {
        log.warn("无操作权限: {}", e.getMessage());
        return Result.error(403, "无操作权限");
    }

    /**
     * 参数校验失败处理
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        log.warn("参数校验失败: {}", e.getMessage());
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.error(400, message);
    }

    /**
     * 系统异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(500, "系统繁忙，请稍后重试");
    }
}
