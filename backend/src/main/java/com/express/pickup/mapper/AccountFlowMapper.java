package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.express.pickup.entity.AccountFlow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 账户流水Mapper
 */
public interface AccountFlowMapper extends BaseMapper<AccountFlow> {

    /**
     * 根据ID查询账户流水详情
     *
     * @param id 账户流水ID
     * @return AccountFlow 账户流水详情
     */
    @Select("SELECT af.*, u.username FROM account_flow af " +
            "LEFT JOIN sys_user u ON af.user_id = u.id WHERE af.id = #{id}")
    AccountFlow selectDetailById(Long id);

    /**
     * 查询账户流水列表
     *
     * @param page     分页参数
     * @param userId   用户ID
     * @param type     流水类型
     * @return IPage<AccountFlow> 账户流水列表
     */
    @Select("<script>" +
            "SELECT af.*, u.username FROM account_flow af " +
            "LEFT JOIN sys_user u ON af.user_id = u.id " +
            "WHERE 1=1 " +
            "<if test='userId != null'>" +
            "AND af.user_id = #{userId}" +
            "</if>" +
            "<if test='type != null'>" +
            "AND af.type = #{type}" +
            "</if>" +
            "ORDER BY af.create_time DESC" +
            "</script>")
    IPage<AccountFlow> selectFlowPage(Page<AccountFlow> page, @Param("userId") Long userId, 
                                       @Param("type") Integer type);
}
