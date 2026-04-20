package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.express.pickup.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据 ID 查询订单的详细信息，同时关联查询多个表，获取订单及相关的人员、驿站、包裹规格和楼栋信息。
     * @param id
     * @return
     */
    @Select("SELECT o.*, u.username, u.nickname as delivery_name, es.station_name, ps.spec_name, db.building_name " +
            "FROM orders o " +
            "LEFT JOIN sys_user u ON o.delivery_id = u.id " +
            "LEFT JOIN express_station es ON o.station_id = es.id " +
            "LEFT JOIN package_spec ps ON o.spec_id = ps.id " +
            "LEFT JOIN dormitory_building db ON o.building_id = db.id " +
            "WHERE o.id = #{id}")
    Order selectDetailById(Long id);

    /**
     * 查询订单列表，同时关联查询多个表，获取订单及相关的人员、驿站、包裹规格和楼栋信息。
     * @param orderNo
     * @param userId
     * @param deliveryId
     * @param status
     * @param stationId
     * @return
     */
    @Select("<script>" +
            "SELECT o.*, u.username, su.nickname as delivery_name, es.station_name, ps.spec_name, db.building_name " +
            "FROM orders o " +
            "LEFT JOIN sys_user u ON o.user_id = u.id " +
            "LEFT JOIN sys_user su ON o.delivery_id = su.id " +
            "LEFT JOIN express_station es ON o.station_id = es.id " +
            "LEFT JOIN package_spec ps ON o.spec_id = ps.id " +
            "LEFT JOIN dormitory_building db ON o.building_id = db.id " +
            "WHERE 1=1 " +
            "<if test='orderNo != null and orderNo != \"\"'>" +
            "AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')" +
            "</if>" +
            "<if test='userId != null'>" +
            "AND o.user_id = #{userId}" +
            "</if>" +
            "<if test='deliveryId != null'>" +
            "AND o.delivery_id = #{deliveryId}" +
            "</if>" +
            "<if test='status != null'>" +
            "AND o.status = #{status}" +
            "</if>" +
            "<if test='stationId != null'>" +
            "AND o.station_id = #{stationId}" +
            "</if>" +
            "ORDER BY o.create_time DESC" +
            "</script>")
    IPage<Order> selectOrderPage(Page<Order> page, @Param("orderNo") String orderNo,
                                  @Param("userId") Long userId, @Param("deliveryId") Long deliveryId,
                                  @Param("status") Integer status, @Param("stationId") Long stationId);

    /**
     * 获取待处理订单列表，同时关联查询多个表，获取订单及相关的人员、驿站、包裹规格和楼栋信息。
     * @param page
     * @return
     */

    @Select("SELECT o.*, u.username, su.nickname as delivery_name, es.station_name, ps.spec_name, db.building_name " +
            "FROM orders o " +
            "LEFT JOIN sys_user u ON o.user_id = u.id " +
            "LEFT JOIN sys_user su ON o.delivery_id = su.id " +
            "LEFT JOIN express_station es ON o.station_id = es.id " +
            "LEFT JOIN package_spec ps ON o.spec_id = ps.id " +
            "LEFT JOIN dormitory_building db ON o.building_id = db.id " +
            "WHERE o.status = 0 AND o.pay_status = 1 " +
            "ORDER BY o.create_time DESC")
    IPage<Order> selectPendingOrderPage(Page<Order> page);

    /**
     * 获取待处理订单列表，同时关联查询多个表，获取订单及相关的人员、驿站、包裹规格和楼栋信息。
     * @return
     */

    @Update("UPDATE orders SET delivery_id = #{deliveryId}, status = 1, receive_time = #{receiveTime} " +
            "WHERE id = #{orderId} AND status = 0")
    int receiveOrder(@Param("orderId") Long orderId, @Param("deliveryId") Long deliveryId, 
                     @Param("receiveTime") LocalDateTime receiveTime);

    /**
     * 完成订单，同时更新订单状态和完成时间。
     * @param orderId
     * @param completeTime
     * @return
     */
    @Update("UPDATE orders SET status = 2, complete_time = #{completeTime} " +
            "WHERE id = #{orderId} AND status = 1")
    int completeOrder(@Param("orderId") Long orderId, @Param("completeTime") LocalDateTime completeTime);

    /**
     * 取消订单，同时更新订单状态和取消时间。
     * @param orderId
     * @param cancelTime
     * @param cancelReason
     * @return
     */
    @Update("UPDATE orders SET status = 3, cancel_time = #{cancelTime}, cancel_reason = #{cancelReason} " +
            "WHERE id = #{orderId} AND status IN (0, 1)")
    int cancelOrder(@Param("orderId") Long orderId, @Param("cancelTime") LocalDateTime cancelTime,
                    @Param("cancelReason") String cancelReason);

    /**
     * 统计订单状态的统计数据。
     * @return
     */
    @Select("SELECT status, COUNT(*) as count FROM orders GROUP BY status")
    List<Map<String, Object>> selectStatusStatistics();



    /**
     * 统计订单的每日统计数据。
     * @param startTime
     * @param endTime
     * @return
     */

    @Select("SELECT DATE(create_time) as date, COUNT(*) as count FROM orders " +
            "WHERE create_time >= #{startTime} AND create_time <= #{endTime} " +
            "GROUP BY DATE(create_time) ORDER BY date")
    List<Map<String, Object>> selectDailyStatistics(@Param("startTime") LocalDateTime startTime, 
                                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 查询订单总数量。
     * @return 订单总记录数
     */
    @Select("SELECT COUNT(*) FROM orders")
    Long selectTotalCount();

    /**
     * 根据订单状态查询订单数量。
     * @param status 订单状态
     * @return 订单数量
     */
    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    Long selectCountByStatus(Integer status);
}
