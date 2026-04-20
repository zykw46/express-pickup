package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.express.pickup.entity.Role;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色编码查询角色信息
     * @param roleCode
     * @return
     */
    @Select("SELECT * FROM sys_role " +
            "WHERE role_code = #{roleCode}")
    Role selectByCode(String roleCode);
}
