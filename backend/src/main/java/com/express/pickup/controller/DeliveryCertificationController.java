package com.express.pickup.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.express.pickup.common.PageQuery;
import com.express.pickup.common.Result;
import com.express.pickup.entity.DeliveryCertification;
import com.express.pickup.service.DeliveryCertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 配送员认证管理控制器
 */

@RestController
@RequestMapping("/certification")
@RequiredArgsConstructor
public class DeliveryCertificationController {

    private final DeliveryCertificationService certificationService;

    /**
     * 获取配送员认证列表
     *
     * @param pageQuery 分页参数
     * @param keyword   关键字
     * @param status    认证状态
     * @return 配送员认证列表
     */
    @GetMapping("/list")
    public Result<IPage<DeliveryCertification>> list(PageQuery pageQuery,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) Integer status) {
        IPage<DeliveryCertification> page = certificationService.getCertificationPage(pageQuery, keyword, status);
        return Result.success(page);
    }

    /**
     * 获取配送员认证详情
     *
     * @param id 配送员认证ID
     * @return 配送员认证详情
     */
    @GetMapping("/detail/{id}")
    public Result<DeliveryCertification> detail(@PathVariable Long id) {
        DeliveryCertification certification = certificationService.getCertificationById(id);
        return Result.success(certification);
    }

    /**
     * 获取当前登录配送员的认证信息
     *
     * @return 配送员认证信息
     */
    @GetMapping("/my")
    public Result<DeliveryCertification> myCertification() {
        DeliveryCertification certification = certificationService.getMyCertification();
        return Result.success(certification);
    }

    /**
     * 提交配送员认证信息
     *
     * @param certification 配送员认证信息
     * @return 提交结果
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody DeliveryCertification certification) {
        certificationService.submitCertification(certification);
        return Result.success("提交成功，请等待审核");
    }

    /**
     * 审核配送员认证信息
     *
     * @param id            配送员认证ID
     * @param status        审核状态
     * @param rejectReason  拒绝原因
     * @return 审核结果
     */
    @PostMapping("/audit/{id}")
    public Result<String> audit(@PathVariable Long id,
                               @RequestParam Integer status,
                               @RequestParam(required = false) String rejectReason) {
        certificationService.auditCertification(id, status, rejectReason);
        return Result.success("审核完成");
    }

    /**
     * 删除配送员认证信息
     *
     * @param id 配送员认证ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return Result.success("删除成功");
    }
}
