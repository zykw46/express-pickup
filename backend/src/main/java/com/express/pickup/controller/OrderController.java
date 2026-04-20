package com.express.pickup.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.express.pickup.common.PageQuery;
import com.express.pickup.common.Result;
import com.express.pickup.entity.Order;
import com.express.pickup.service.OrderService;
import com.express.pickup.vo.CreateOrderVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 获取订单列表
     *
     * @param pageQuery 分页查询参数
     * @param orderNo 订单编号
     * @param userId 用户ID
     * @param deliveryId 配送员ID
     * @param status 订单状态
     * @param stationId 配送站ID
     * @return Result<IPage<Order>> 订单列表
     */
    @GetMapping("/list")
    public Result<IPage<Order>> list(PageQuery pageQuery,
                                      @RequestParam(required = false) String orderNo,
                                      @RequestParam(required = false) Long userId,
                                      @RequestParam(required = false) Long deliveryId,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam(required = false) Long stationId) {
        IPage<Order> page = orderService.getOrderPage(pageQuery, orderNo, userId, deliveryId, status, stationId);
        return Result.success(page);
    }

    /**
     * 获取待处理订单列表
     *
     * @param pageQuery 分页查询参数
     * @return Result<IPage<Order>> 待处理订单列表
     */
    @GetMapping("/pending-list")
    public Result<IPage<Order>> pendingList(PageQuery pageQuery) {
        IPage<Order> page = orderService.getPendingOrderPage(pageQuery);
        return Result.success(page);
    }

    /**
     * 获取订单详情
     *
     * @param id 订单ID
     * @return Result<Order> 订单详情
     */
    @GetMapping("/detail/{id}")
    public Result<Order> detail(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        return Result.success(order);
    }

    /**
     * 获取当前用户的订单列表
     *
     * @param status 订单状态
     * @return Result<List<Order>> 当前用户的订单列表
     */
    @GetMapping("/my-orders")
    public Result<List<Order>> myOrders(@RequestParam(required = false) Integer status) {
        Long userId = StpUtil.getLoginIdAsLong();
        List<Order> list = orderService.getUserOrders(userId, status);
        return Result.success(list);
    }

    /**
     * 获取当前配送员的订单列表
     *
     * @param status 订单状态
     * @return Result<List<Order>> 当前配送员的订单列表
     */
    @GetMapping("/delivery-orders")
    public Result<List<Order>> deliveryOrders(@RequestParam(required = false) Integer status) {
        Long deliveryId = StpUtil.getLoginIdAsLong();
        List<Order> list = orderService.getDeliveryOrders(deliveryId, status);
        return Result.success(list);
    }

    /**
     * 创建订单
     *
     * @param orderVO 创建订单参数
     * @return Result<Order> 创建的订单信息
     */

    @PostMapping("/create")
    public Result<Order> create(@Valid @RequestBody CreateOrderVO orderVO) {
        Order order = orderService.createOrder(orderVO);
        return Result.success("创建成功", order);
    }

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @return Result<String> 支付结果
     */
    @PostMapping("/pay/{orderId}")
    public Result<String> pay(@PathVariable Long orderId) {
        orderService.payOrder(orderId);
        return Result.success("支付成功");
    }

    /**
     * 接单
     *
     * @param orderId 订单ID
     * @return Result<String> 接单结果
     */
    @PostMapping("/receive/{orderId}")
    public Result<String> receive(@PathVariable Long orderId) {
        orderService.receiveOrder(orderId);
        return Result.success("接单成功");
    }

    /**
     * 完成订单
     *
     * @param orderId 订单ID
     * @return Result<String> 完成订单结果
     */

    @PostMapping("/complete/{orderId}")
    public Result<String> complete(@PathVariable Long orderId) {
        orderService.completeOrder(orderId);
        return Result.success("订单已完成");
    }

    /**
     * 确认收货
     *
     * @param orderId 订单ID
     * @return Result<String> 确认收货结果
     */
    @PostMapping("/confirm/{orderId}")
    public Result<String> confirm(@PathVariable Long orderId) {
        orderService.confirmOrder(orderId);
        return Result.success("确认收货成功");
    }

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param reason 取消原因
     * @return Result<String> 取消订单结果
     */
    @PostMapping("/cancel/{orderId}")
    public Result<String> cancel(@PathVariable Long orderId,
                                 @RequestParam(required = false) String reason) {
        orderService.cancelOrder(orderId, reason);
        return Result.success("取消成功");
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    @DeleteMapping("/delete/{orderId}")
    public Result<String> delete(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return Result.success("删除成功");
    }
}
