package com.express.pickup.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.common.BusinessException;
import com.express.pickup.common.PageQuery;
import com.express.pickup.entity.*;
import com.express.pickup.mapper.*;
import com.express.pickup.utils.OrderNoUtil;
import com.express.pickup.vo.CreateOrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final ExpressStationMapper stationMapper;
    private final PackageSpecMapper specMapper;
    private final DormitoryBuildingMapper buildingMapper;
    private final AccountFlowMapper accountFlowMapper;
    private final DeliveryCertificationMapper certificationMapper;

    @Value("${express.order.base-price:2.00}")
    private BigDecimal basePrice;

    @Value("${express.order.delivery-rate:0.70}")
    private BigDecimal deliveryRate;

    public IPage<Order> getOrderPage(PageQuery pageQuery, String orderNo, Long userId,
                                     Long deliveryId, Integer status, Long stationId) {
        Page<Order> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return orderMapper.selectOrderPage(page, orderNo, userId, deliveryId, status, stationId);
    }

    public IPage<Order> getPendingOrderPage(PageQuery pageQuery) {
        Page<Order> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return orderMapper.selectPendingOrderPage(page);
    }

    public Order getOrderDetail(Long id) {
        return orderMapper.selectDetailById(id);
    }

    public List<Order> getUserOrders(Long userId, Integer status) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(wrapper);
    }

    public List<Order> getDeliveryOrders(Long deliveryId, Integer status) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(Order::getDeliveryId, deliveryId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(CreateOrderVO orderVO) {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        ExpressStation station = stationMapper.selectById(orderVO.getStationId());
        if (station == null || station.getStatus() == 0) {
            throw new BusinessException("快递站点不存在或已禁用");
        }

        PackageSpec spec = specMapper.selectById(orderVO.getSpecId());
        if (spec == null || spec.getStatus() == 0) {
            throw new BusinessException("包裹规格不存在或已禁用");
        }

        DormitoryBuilding building = buildingMapper.selectById(orderVO.getBuildingId());
        if (building == null || building.getStatus() == 0) {
            throw new BusinessException("宿舍楼不存在或已禁用");
        }

        BigDecimal extraPrice = spec.getExtraPrice() != null ? spec.getExtraPrice() : BigDecimal.ZERO;
        BigDecimal totalPrice = basePrice.add(extraPrice);
        BigDecimal deliveryIncome = totalPrice.multiply(deliveryRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal platformIncome = totalPrice.subtract(deliveryIncome);

        Order order = new Order();
        order.setOrderNo(OrderNoUtil.generateOrderNo());
        order.setUserId(userId);
        order.setStationId(orderVO.getStationId());
        order.setSpecId(orderVO.getSpecId());
        order.setPickupCode(orderVO.getPickupCode());
        order.setBuildingId(orderVO.getBuildingId());
        order.setRoomNumber(orderVO.getRoomNumber());
        order.setRemark(orderVO.getRemark());
        order.setBasePrice(basePrice);
        order.setExtraPrice(extraPrice);
        order.setTotalPrice(totalPrice);
        order.setDeliveryIncome(deliveryIncome);
        order.setPlatformIncome(platformIncome);
        order.setStatus(0);
        order.setPayStatus(0);

        orderMapper.insert(order);
        return order;
    }

    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId) {
        long userId = StpUtil.getLoginIdAsLong();
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getPayStatus() == 1) {
            throw new BusinessException("订单已支付");
        }
        if (order.getStatus() == 3) {
            throw new BusinessException("订单已取消");
        }

        User user = userMapper.selectById(userId);
        if (user.getBalance().compareTo(order.getTotalPrice()) < 0) {
            throw new BusinessException("账户余额不足，请先充值");
        }

        int result = userMapper.subtractBalance(userId, order.getTotalPrice());
        if (result <= 0) {
            throw new BusinessException("余额扣除失败");
        }

        order.setPayStatus(1);
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);

        AccountFlow flow = new AccountFlow();
        flow.setUserId(userId);
        flow.setType(2);
        flow.setAmount(order.getTotalPrice().negate());
        flow.setBalance(user.getBalance().subtract(order.getTotalPrice()));
        flow.setRemark("订单支付：" + order.getOrderNo());
        flow.setRelatedId(orderId);
        accountFlowMapper.insert(flow);
    }

    @Transactional(rollbackFor = Exception.class)
    public void receiveOrder(Long orderId) {
        long deliveryId = StpUtil.getLoginIdAsLong();

        DeliveryCertification certification = certificationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DeliveryCertification>()
                        .eq(DeliveryCertification::getUserId, deliveryId)
                        .eq(DeliveryCertification::getStatus, 2)
        );

        if (certification == null) {
            throw new BusinessException("您还未通过配送员认证，无法接单");
        }

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态不正确");
        }
        if (order.getPayStatus() != 1) {
            throw new BusinessException("订单未支付");
        }

        int result = orderMapper.receiveOrder(orderId, deliveryId, LocalDateTime.now());
        if (result <= 0) {
            throw new BusinessException("接单失败，订单可能已被其他配送员接单");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void completeOrder(Long orderId) {
        long deliveryId = StpUtil.getLoginIdAsLong();
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getDeliveryId().equals(deliveryId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("订单状态不正确");
        }

        int result = orderMapper.completeOrder(orderId, LocalDateTime.now());
        if (result <= 0) {
            throw new BusinessException("完成订单失败");
        }

        userMapper.addBalance(deliveryId, order.getDeliveryIncome());

        User delivery = userMapper.selectById(deliveryId);
        AccountFlow flow = new AccountFlow();
        flow.setUserId(deliveryId);
        flow.setType(3);
        flow.setAmount(order.getDeliveryIncome());
        flow.setBalance(delivery.getBalance().add(order.getDeliveryIncome()));
        flow.setRemark("配送收入：" + order.getOrderNo());
        flow.setRelatedId(orderId);
        accountFlowMapper.insert(flow);
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirmOrder(Long orderId) {
        long userId = StpUtil.getLoginIdAsLong();
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("订单状态不正确");
        }

        int result = orderMapper.completeOrder(orderId, LocalDateTime.now());
        if (result <= 0) {
            throw new BusinessException("确认收货失败");
        }

        if (order.getDeliveryId() != null) {
            userMapper.addBalance(order.getDeliveryId(), order.getDeliveryIncome());

            User delivery = userMapper.selectById(order.getDeliveryId());
            AccountFlow flow = new AccountFlow();
            flow.setUserId(order.getDeliveryId());
            flow.setType(3);
            flow.setAmount(order.getDeliveryIncome());
            flow.setBalance(delivery.getBalance().add(order.getDeliveryIncome()));
            flow.setRemark("配送收入：" + order.getOrderNo());
            flow.setRelatedId(orderId);
            accountFlowMapper.insert(flow);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, String reason) {
        long userId = StpUtil.getLoginIdAsLong();
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() != 0 && order.getStatus() != 1) {
            throw new BusinessException("订单状态不正确，无法取消");
        }

        int result = orderMapper.cancelOrder(orderId, LocalDateTime.now(), reason);
        if (result <= 0) {
            throw new BusinessException("取消订单失败");
        }

        if (order.getPayStatus() == 1) {
            userMapper.addBalance(userId, order.getTotalPrice());

            User user = userMapper.selectById(userId);
            AccountFlow flow = new AccountFlow();
            flow.setUserId(userId);
            flow.setType(1);
            flow.setAmount(order.getTotalPrice());
            flow.setBalance(user.getBalance().add(order.getTotalPrice()));
            flow.setRemark("订单退款：" + order.getOrderNo());
            flow.setRelatedId(orderId);
            accountFlowMapper.insert(flow);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 3) {
            throw new BusinessException("只能删除已取消的订单");
        }
        orderMapper.deleteById(orderId);
    }
}