package com.express.pickup.controller;

import com.express.pickup.common.Result;
import com.express.pickup.utils.CaptchaUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController  // 表示该类是一个 RESTful 风格的控制器
@RequestMapping("/captcha")
@RequiredArgsConstructor // 自动生成构造函数
public class CaptchaController {

    /**
     * 生成验证码图片
     *
     * @param session HttpSession 对象
     * @return Result<Map<String, String>> 验证码图片和验证码字符串的返回结果
     */
    @GetMapping("/generate")
      //验证码图片和验证码字符串的返回结果
    public Result<Map<String, String>> generateCaptcha(HttpSession session) throws IOException {
        String code = CaptchaUtil.generateCode();
        String imageBase64 = CaptchaUtil.generateCaptchaImage(code);

        session.setAttribute("captchaCode", code); // 将验证码保存到 Session 中

        Map<String, String> result = new HashMap<>(); // 创建一个结果对象
        result.put("image", imageBase64); // 将验证码图片和验证码字符串添加到结果对象中

        return Result.success(result);
    }
}
