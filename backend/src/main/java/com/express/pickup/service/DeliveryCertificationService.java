package com.express.pickup.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.common.BusinessException;
import com.express.pickup.common.PageQuery;
import com.express.pickup.entity.DeliveryCertification;
import com.express.pickup.entity.Role;
import com.express.pickup.entity.User;
import com.express.pickup.mapper.DeliveryCertificationMapper;
import com.express.pickup.mapper.RoleMapper;
import com.express.pickup.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 配送人员认证服务业务
 */
@Service
@RequiredArgsConstructor
public class DeliveryCertificationService extends ServiceImpl<DeliveryCertificationMapper, DeliveryCertification> {

    private final DeliveryCertificationMapper certificationMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public IPage<DeliveryCertification> getCertificationPage(PageQuery pageQuery, String keyword, Integer status) {
        Page<DeliveryCertification> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return certificationMapper.selectCertificationPage(page, keyword, status);
    }

    public DeliveryCertification getCertificationById(Long id) {
        return certificationMapper.selectDetailById(id);
    }

    public DeliveryCertification getMyCertification() {
        long userId = StpUtil.getLoginIdAsLong();
        return certificationMapper.selectByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitCertification(DeliveryCertification certification) {
        long userId = StpUtil.getLoginIdAsLong();
        
        DeliveryCertification existCert = certificationMapper.selectByUserId(userId);
        if (existCert != null && existCert.getStatus() == 1) {
            throw new BusinessException("认证申请正在审核中");
        }
        if (existCert != null && existCert.getStatus() == 2) {
            throw new BusinessException("您已通过认证");
        }
        
        certification.setUserId(userId);
        certification.setStatus(1);
        certification.setSubmitTime(LocalDateTime.now());
        
        if (existCert != null) {
            certification.setId(existCert.getId());
            certificationMapper.updateById(certification);
        } else {
            certificationMapper.insert(certification);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditCertification(Long id, Integer status, String rejectReason) {
        DeliveryCertification certification = certificationMapper.selectById(id);
        if (certification == null) {
            throw new BusinessException("认证记录不存在");
        }
        if (certification.getStatus() != 1) {
            throw new BusinessException("该申请已审核");
        }
        
        certification.setStatus(status);
        certification.setAuditTime(LocalDateTime.now());
        certification.setAuditBy(StpUtil.getLoginIdAsLong());
        
        if (status == 3) {
            if (rejectReason == null || rejectReason.isEmpty()) {
                throw new BusinessException("请填写拒绝原因");
            }
            certification.setRejectReason(rejectReason);
        }
        
        certificationMapper.updateById(certification);
        
        if (status == 2) {
            User user = userMapper.selectById(certification.getUserId());
            if (user != null) {
                Role deliveryRole = roleMapper.selectByCode("delivery");
                if (deliveryRole != null) {
                    user.setRoleId(deliveryRole.getId());
                    userMapper.updateById(user);
                }
            }
        }
    }

    public void deleteCertification(Long id) {
        certificationMapper.deleteById(id);
    }
}
