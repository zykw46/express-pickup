package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.express.pickup.entity.DeliveryCertification;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DeliveryCertificationMapper extends BaseMapper<DeliveryCertification> {

    /**
     * 根据 ID 查询配送认证的详细信息，同时关联查询用户表，获取认证记录及对应的用户名和昵称。
     * @param id
     * @return
     */
    @Select("SELECT dc.*, u.username, u.nickname " +
            "FROM delivery_certification dc " +
            "LEFT JOIN sys_user u ON dc.user_id = u.id " +
            "WHERE dc.id = #{id}")
    DeliveryCertification selectDetailById(Long id);

    /**
     * 配送认证的分页查询
     * @param page
     * @param keyword
     * @param status
     * @return
     */
    @Select("<script>" +
            "SELECT dc.*, u.username, u.nickname " +
            "FROM delivery_certification dc " +
            "LEFT JOIN sys_user u ON dc.user_id = u.id " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (dc.real_name LIKE CONCAT('%', #{keyword}, '%') OR dc.phone LIKE CONCAT('%', #{keyword}, '%') OR u.username LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "<if test='status != null'>" +
            "AND dc.status = #{status}" +
            "</if>" +
            "ORDER BY dc.create_time DESC" +
            "</script>")
    IPage<DeliveryCertification> selectCertificationPage(Page<DeliveryCertification> page, 
                                                          @Param("keyword") String keyword, 
                                                          @Param("status") Integer status);

    @Select("SELECT * FROM delivery_certification WHERE user_id = #{userId}")
    DeliveryCertification selectByUserId(Long userId);
}
