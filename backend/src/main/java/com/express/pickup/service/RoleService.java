package com.express.pickup.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.common.BusinessException;
import com.express.pickup.entity.Role;
import com.express.pickup.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    private final RoleMapper roleMapper;

    public List<Role> getAllRoles() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Role::getId);
        return roleMapper.selectList(wrapper);
    }

    public Role getRoleById(Long id) {
        return roleMapper.selectById(id);
    }

    public void addRole(Role role) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, role.getRoleCode());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色编码已存在");
        }
        roleMapper.insert(role);
    }

    public void updateRole(Role role) {
        Role existRole = roleMapper.selectById(role.getId());
        if (existRole == null) {
            throw new BusinessException("角色不存在");
        }
        
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, role.getRoleCode());
        wrapper.ne(Role::getId, role.getId());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色编码已存在");
        }
        
        roleMapper.updateById(role);
    }

    public void deleteRole(Long id) {
        roleMapper.deleteById(id);
    }
}
