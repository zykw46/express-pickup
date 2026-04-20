package com.express.pickup.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.express.pickup.common.PageQuery;
import com.express.pickup.common.Result;
import com.express.pickup.entity.User;
import com.express.pickup.service.UserService;
import com.express.pickup.vo.UserUpdateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户列表
     * @param pageQuery 分页参数
     * @param keyword 关键字
     * @param roleId 角色ID
     * @param status 状态
     */
    @GetMapping("/list")
    public Result<IPage<User>> list(PageQuery pageQuery, 
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Long roleId,
                                    @RequestParam(required = false) Integer status) {
        IPage<User> page = userService.getUserPage(pageQuery, keyword, roleId, status);
        return Result.success(page);
    }

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/detail/{id}")
    public Result<User> detail(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 添加用户
     * @param user 用户信息
     * @return 添加结果
     * @throws Exception 添加失败
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody User user) {
        userService.addUser(user);
        return Result.success("添加成功");
    }

    /**
     * 修改用户信息
     * @param userVO 用户信息
     * @return 修改结果
     * @throws Exception 修改失败
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody UserUpdateVO userVO) {
        userService.updateUser(userVO);
        return Result.success("修改成功");
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     * @throws Exception 删除失败
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}
