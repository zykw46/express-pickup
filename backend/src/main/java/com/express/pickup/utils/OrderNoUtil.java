package com.express.pickup.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;

public class OrderNoUtil {

    /**
     * 生成订单号
     *
     * @return
     */
    public static String generateOrderNo() {
        String dateStr = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String randomStr = RandomUtil.randomNumbers(6);
        return "EX" + dateStr + randomStr;
    }
}
