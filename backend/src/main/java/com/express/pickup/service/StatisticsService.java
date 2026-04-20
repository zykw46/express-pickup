package com.express.pickup.service;

import com.express.pickup.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final NoticeMapper noticeMapper;
    private final DeliveryCertificationMapper certificationMapper;

    public Map<String, Object> getHomeStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        Long totalOrder = orderMapper.selectTotalCount();
        Long totalUser = userMapper.selectCount(null);
        Long totalDelivery = certificationMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.express.pickup.entity.DeliveryCertification>()
                .eq(com.express.pickup.entity.DeliveryCertification::getStatus, 2)
        );
        Long totalNotice = noticeMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.express.pickup.entity.Notice>()
                .eq(com.express.pickup.entity.Notice::getStatus, 1)
        );
        
        result.put("totalOrder", totalOrder);
        result.put("totalUser", totalUser);
        result.put("totalDelivery", totalDelivery);
        result.put("totalNotice", totalNotice);
        
        return result;
    }

    public List<Map<String, Object>> getOrderStatusStatistics() {
        return orderMapper.selectStatusStatistics();
    }

    public List<Map<String, Object>> getOrderDailyStatistics(int days) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);
        return orderMapper.selectDailyStatistics(startTime, endTime);
    }
}
