package com.express.pickup.controller;

import com.express.pickup.common.Result;
import com.express.pickup.entity.Role;
import com.express.pickup.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 获取所有角色
     * @return
     */
    @GetMapping("/list")
    public Result<List<Role>> list() {
        List<Role> list = roleService.getAllRoles();
        return Result.success(list);
    }

    /**
     * 获取角色详情
     * @param id
     * @return
     */

    @GetMapping("/detail/{id}")
    public Result<Role> detail(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success("添加成功");
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success("修改成功");
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success("删除成功");
    }
}
