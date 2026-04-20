package com.express.pickup.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaUtil {

    private static final int WIDTH = 120;      // 图片宽度
    private static final int HEIGHT = 40;      // 图片高度
    private static final int CODE_LENGTH = 4;  // 验证码位数
    private static final Random RANDOM = new Random();

    /**
     * 生成 4 位随机数字字符串
     */
    public static String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(RANDOM.nextInt(10));  // 0~9 随机数字
        }
        return sb.toString();
    }

    /**
     * 根据验证码字符串生成图片，返回 Base64 编码的字符串（可直接用于 img 标签）
     */
    public static String generateCaptchaImage(String code) throws IOException {
        // 1. 创建图像缓冲区
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 2. 填充背景色（白色）
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 3. 画边框
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // 4. 画干扰线（让机器更难识别）
        g.setColor(Color.GRAY);
        for (int i = 0; i < 5; i++) {
            int x1 = RANDOM.nextInt(WIDTH);
            int y1 = RANDOM.nextInt(HEIGHT);
            int x2 = RANDOM.nextInt(WIDTH);
            int y2 = RANDOM.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 5. 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // 6. 逐个绘制字符，每个字符颜色随机
        for (int i = 0; i < code.length(); i++) {
            // 随机颜色（偏深色，保证可读）
            g.setColor(new Color(
                    RANDOM.nextInt(100),   // red 0~100
                    RANDOM.nextInt(150),   // green 0~150
                    RANDOM.nextInt(200)    // blue 0~200
            ));
            // 绘制单个字符，位置错开一点
            g.drawString(String.valueOf(code.charAt(i)), 20 * i + 15, 28);
        }

        g.dispose();

        // 7. 将图片写入字节数组，并转为 Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }
}