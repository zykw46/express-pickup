package com.express.pickup.controller;

import com.express.pickup.common.Result;
import com.express.pickup.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 */

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取首页统计数据
     *
     * @return
     */
    @GetMapping("/home")
    public Result<Map<String, Object>> getHomeStatistics() {
        Map<String, Object> result = statisticsService.getHomeStatistics();
        return Result.success(result);
    }

    /**
     * 获取订单状态统计数据
     *
     * @return
     */
    @GetMapping("/order-status")
    public Result<List<Map<String, Object>>> getOrderStatusStatistics() {
        List<Map<String, Object>> result = statisticsService.getOrderStatusStatistics();
        return Result.success(result);
    }

    /**
     * 获取订单日统计数据
     *
     * @param days
     * @return
     */
    @GetMapping("/order-daily")
    public Result<List<Map<String, Object>>> getOrderDailyStatistics(
            @RequestParam(defaultValue = "7") int days) {
        List<Map<String, Object>> result = statisticsService.getOrderDailyStatistics(days);
        return Result.success(result);
    }
}
