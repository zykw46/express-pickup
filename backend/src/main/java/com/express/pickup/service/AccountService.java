package com.express.pickup.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.common.BusinessException;
import com.express.pickup.common.PageQuery;
import com.express.pickup.entity.AccountFlow;
import com.express.pickup.entity.User;
import com.express.pickup.mapper.AccountFlowMapper;
import com.express.pickup.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 账户额度业务
 */
@Service
@RequiredArgsConstructor
public class AccountService extends ServiceImpl<AccountFlowMapper, AccountFlow> {

    private final AccountFlowMapper accountFlowMapper;
    private final UserMapper userMapper;

    /**
     * 获取当前登录用户的账户余额。
     * @return 用户账户余额
     */
    public BigDecimal getBalance() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user.getBalance();
    }

    /**
     * 分页查询用户的账户流水记录。
     * @param pageQuery 分页查询参数，包含页码和每页大小
     * @param type 流水类型，用于筛选特定类型的流水记录
     * @return 账户流水的分页结果
     */
    public IPage<AccountFlow> getFlowPage(PageQuery pageQuery, Integer type) {
        long userId = StpUtil.getLoginIdAsLong();
        Page<AccountFlow> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return accountFlowMapper.selectFlowPage(page, userId, type);
    }

    /**
     * 为用户账户进行充值操作，增加账户余额并记录充值流水。
     * @param amount 充值金额，必须大于 0
     */
    @Transactional(rollbackFor = Exception.class)
    public void recharge(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }
        
        long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        //添加用户余额
        userMapper.addBalance(userId, amount);

        //添加流水记录
        AccountFlow flow = new AccountFlow();
        flow.setUserId(userId);
        flow.setType(1);
        flow.setAmount(amount);
        flow.setBalance(user.getBalance().add(amount));
        flow.setRemark("账户充值");
        accountFlowMapper.insert(flow);
    }
}
