package com.express.pickup.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.express.pickup.common.BusinessException;
import com.express.pickup.common.Result;
import com.express.pickup.entity.User;
import com.express.pickup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UserService userService;

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @Value("${upload.max-size:5242880}")
    private long maxSize;

    @PostMapping("/avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("请选择要上传的文件");
        }

        long size = file.getSize();
        if (size > maxSize) {
            throw new BusinessException("文件大小不能超过5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("只能上传图片文件");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException("文件名不能为空");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
        boolean isAllowed = false;
        for (String ext : allowedExtensions) {
            if (ext.equalsIgnoreCase(extension)) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            throw new BusinessException("只支持jpg、jpeg、png、gif、webp格式的图片");
        }

        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;
            Path filePath = Paths.get(uploadPath, fileName);
            Files.write(filePath, file.getBytes());

            String avatarUrl = "/uploads/" + fileName;

            long userId = StpUtil.getLoginIdAsLong();
            User user = userService.getById(userId);
            if (user != null) {
                user.setAvatar(avatarUrl);
                userService.updateById(user);
            }

            Map<String, String> result = new HashMap<>();
            result.put("url", avatarUrl);
            return Result.success(result);
        } catch (IOException e) {
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }
}
