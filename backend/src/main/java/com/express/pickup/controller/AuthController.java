package com.express.pickup.controller;

import com.express.pickup.common.Result;
import com.express.pickup.service.UserService;
import com.express.pickup.vo.LoginVO;
import com.express.pickup.vo.RegisterVO;
import com.express.pickup.vo.UserUpdateVO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginVO loginVO, HttpSession session) {
        Map<String, Object> result = userService.login(loginVO, session);
        return Result.success("登录成功", result);
    }


    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterVO registerVO) {
        userService.register(registerVO);
        return Result.success("注册成功");
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        userService.logout();
        return Result.success("退出成功");
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getCurrentUser() {
        Map<String, Object> result = userService.getCurrentUser();
        return Result.success(result);
    }

    @PutMapping("/profile")
    public Result<String> updateProfile(@RequestBody UserUpdateVO userVO) {
        userService.updateProfile(userVO);
        return Result.success("修改成功");
    }

    @PutMapping("/password")
    public Result<String> resetPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.resetPassword(oldPassword, newPassword);
        return Result.success("密码修改成功");
    }
}
