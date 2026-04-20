package com.express.pickup.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.common.BusinessException;
import com.express.pickup.common.PageQuery;
import com.express.pickup.entity.Role;
import com.express.pickup.entity.User;
import com.express.pickup.mapper.RoleMapper;
import com.express.pickup.mapper.UserMapper;
import com.express.pickup.vo.LoginVO;
import com.express.pickup.vo.RegisterVO;
import com.express.pickup.vo.UserUpdateVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Map<String, Object> login(LoginVO loginVO, HttpSession session) {
        String sessionCaptcha = (String) session.getAttribute("captchaCode");
        if (!StringUtils.hasText(sessionCaptcha)) {
            throw new BusinessException("验证码已过期，请刷新");
        }
        
        if (!StringUtils.hasText(loginVO.getCaptchaCode())) {
            throw new BusinessException("请输入验证码");
        }
        
        if (!sessionCaptcha.equalsIgnoreCase(loginVO.getCaptchaCode())) {
            throw new BusinessException("验证码错误");
        }
        
        session.removeAttribute("captchaCode");
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginVO.getUsername());
        User user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        if (!passwordEncoder.matches(loginVO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        Role role = roleMapper.selectById(user.getRoleId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("roleCode", role != null ? role.getRoleCode() : "");
        result.put("roleName", role != null ? role.getRoleName() : "");
        
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterVO registerVO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerVO.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        if (StringUtils.hasText(registerVO.getPhone())) {
            LambdaQueryWrapper<User> phoneWrapper = new LambdaQueryWrapper<>();
            phoneWrapper.eq(User::getPhone, registerVO.getPhone());
            if (userMapper.selectCount(phoneWrapper) > 0) {
                throw new BusinessException("手机号已被注册");
            }
        }
        
        User user = new User();
        user.setUsername(registerVO.getUsername());
        user.setPassword(passwordEncoder.encode(registerVO.getPassword()));
        user.setNickname(registerVO.getNickname());
        user.setPhone(registerVO.getPhone());
        user.setBalance(BigDecimal.ZERO);
        user.setStatus(1);
        
        Role userRole = roleMapper.selectByCode("user");
        if (userRole != null) {
            user.setRoleId(userRole.getId());
        }
        
        userMapper.insert(user);
    }

    public void logout() {
        StpUtil.logout();
    }

    public Map<String, Object> getCurrentUser() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectUserWithRole(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("phone", user.getPhone());
        result.put("email", user.getEmail());
        result.put("balance", user.getBalance());
        result.put("roleId", user.getRoleId());
        result.put("roleName", user.getRoleName());
        
        return result;
    }

    public IPage<User> getUserPage(PageQuery pageQuery, String keyword, Long roleId, Integer status) {
        Page<User> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return userMapper.selectUserPage(page, keyword, roleId, status);
    }

    public User getUserById(Long id) {
        return userMapper.selectUserWithRole(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addUser(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(BigDecimal.ZERO);
        userMapper.insert(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateVO userVO) {
        User user = userMapper.selectById(userVO.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setNickname(userVO.getNickname());
        user.setPhone(userVO.getPhone());
        user.setEmail(userVO.getEmail());
        user.setRoleId(userVO.getRoleId());
        user.setStatus(userVO.getStatus());
        
        if (StringUtils.hasText(userVO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userVO.getPassword()));
        }
        
        userMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        if (id.equals(StpUtil.getLoginIdAsLong())) {
            throw new BusinessException("不能删除当前登录账号");
        }
        userMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(UserUpdateVO userVO) {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setNickname(userVO.getNickname());
        user.setPhone(userVO.getPhone());
        user.setEmail(userVO.getEmail());
        
        if (StringUtils.hasText(userVO.getAvatar())) {
            user.setAvatar(userVO.getAvatar());
        }
        
        userMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String oldPassword, String newPassword) {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
}
