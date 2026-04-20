package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.express.pickup.entity.Notice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 根据 ID 查询公告详情，包含创建人信息
     * 
     * @param id 公告 ID
     * @return Notice 公告详情对象
     */
    @Select("SELECT n.*, u.nickname as create_by_name " +
            "FROM notice n " +
            "LEFT JOIN sys_user u ON n.create_by = u.id " +
            "WHERE n.id = #{id}")
    Notice selectDetailById(Long id);

    /**
     * 分页查询公告列表，支持条件筛选
     * 
     * @param page 分页参数
     * @param title 公告标题，可选参数，支持模糊查询
     * @param status 公告状态，可选参数
     * @return IPage<Notice> 分页公告列表
     */
    @Select("<script>" +
            "SELECT n.*, u.nickname as create_by_name FROM notice n " +
            "LEFT JOIN sys_user u ON n.create_by = u.id " +
            "WHERE 1=1 " +
            "<if test='title != null and title != \"\"'>" +
            "AND n.title LIKE CONCAT('%', #{title}, '%')" +
            "</if>" +
            "<if test='status != null'>" +
            "AND n.status = #{status}" +
            "</if>" +
            "ORDER BY n.is_top DESC, n.create_time DESC" +
            "</script>")
    IPage<Notice> selectNoticePage(Page<Notice> page, @Param("title") String title, 
                                    @Param("status") Integer status);

    /**
     * 查询已发布的公告列表（按置顶和发布时间排序）
     * 
     * @param limit 返回数量限制
     * @return List<Notice> 公告列表
     */
    @Select("SELECT n.*, u.nickname as create_by_name FROM notice n " +
            "LEFT JOIN sys_user u ON n.create_by = u.id " +
            "WHERE n.status = 1 ORDER BY n.is_top DESC, n.create_time DESC LIMIT #{limit}")
    List<Notice> selectActiveList(Integer limit);
}
