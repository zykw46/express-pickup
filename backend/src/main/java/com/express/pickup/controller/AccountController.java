package com.express.pickup.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.express.pickup.common.PageQuery;
import com.express.pickup.common.Result;
import com.express.pickup.entity.AccountFlow;
import com.express.pickup.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 账户管理控制器
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * 查询账户余额
     * 
     * @return Result<Map<String, Object>> 包含余额信息的返回结果
     */
    @GetMapping("/balance")
    public Result<Map<String, Object>> getBalance() {
        BigDecimal balance = accountService.getBalance();
        Map<String, Object> result = new HashMap<>();
        result.put("balance", balance);
        return Result.success(result);
    }

    /**
     * 查询账户流水列表
     * 
     * @param pageQuery 分页查询参数
     * @param type 流水类型，可选参数
     * @return Result<IPage<AccountFlow>> 分页流水数据
     */
    @GetMapping("/flow")
    public Result<IPage<AccountFlow>> getFlowList(PageQuery pageQuery, 
                                                   @RequestParam(required = false) Integer type) {
        IPage<AccountFlow> page = accountService.getFlowPage(pageQuery, type);
        return Result.success(page);
    }

    /**
     * 账户充值
     * 
     * @param amount 充值金额
     * @return Result<String> 操作结果提示
     */
    @PostMapping("/recharge")
    public Result<String> recharge(@RequestParam BigDecimal amount) {
        accountService.recharge(amount);
        return Result.success("充值成功");
    }
}
