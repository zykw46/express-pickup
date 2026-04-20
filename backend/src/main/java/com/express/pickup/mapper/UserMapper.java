package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.express.pickup.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID查询用户信息，包含角色信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Select("SELECT u.*, r.role_name " +
            "FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role_id = r.id " +
            "WHERE u.id = #{id}")
    User selectUserWithRole(Long id);

    /**
     * 分页查询用户列表
     *
     * @param page     分页参数
     * @param keyword  搜索关键词
     * @param roleId   角色ID
     * @param status   状态
     * @return 用户列表
     */
    @Select("<script>" +
            "SELECT u.*, r.role_name " +
            "FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role_id = r.id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (u.username LIKE CONCAT('%', #{keyword}, '%') OR u.nickname LIKE CONCAT('%', #{keyword}, '%') OR u.phone LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "<if test='roleId != null'>" +
            "AND u.role_id = #{roleId}" +
            "</if>" +
            "<if test='status != null'>" +
            "AND u.status = #{status}" +
            "</if>" +
            "ORDER BY u.create_time DESC" +
            "</script>")
    IPage<User> selectUserPage(Page<User> page, @Param("keyword") String keyword, 
                               @Param("roleId") Long roleId, @Param("status") Integer status);

    /**
     * 添加用户余额
     *
     * @param userId 用户ID
     * @param amount 金额
     * @return 影响行数
     */
    @Update("UPDATE sys_user SET balance = balance + #{amount} WHERE id = #{userId}")
    int addBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);

    /**
     * 减去用户余额
     *
     * @param userId 用户ID
     * @param amount 金额
     * @return 影响行数
     */
    @Update("UPDATE sys_user SET balance = balance - #{amount} WHERE id = #{userId} AND balance >= #{amount}")
    int subtractBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}
