package com.express.pickup.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.common.BusinessException;
import com.express.pickup.common.PageQuery;
import com.express.pickup.entity.Notice;
import com.express.pickup.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;



@Service
@RequiredArgsConstructor
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    private final NoticeMapper noticeMapper;

    public IPage<Notice> getNoticePage(PageQuery pageQuery, String title, Integer status) {
        Page<Notice> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return noticeMapper.selectNoticePage(page, title, status);
    }

    public List<Notice> getActiveList(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return noticeMapper.selectActiveList(limit);
    }

    public Notice getNoticeById(Long id) {
        return noticeMapper.selectDetailById(id);
    }

    public void addNotice(Notice notice) {
        notice.setCreateBy(StpUtil.getLoginIdAsLong());
        if (notice.getPublishDate() == null) {
            notice.setPublishDate(LocalDate.now());
        }
        noticeMapper.insert(notice);
    }

    public void updateNotice(Notice notice) {
        Notice existNotice = noticeMapper.selectById(notice.getId());
        if (existNotice == null) {
            throw new BusinessException("公告不存在");
        }
        noticeMapper.updateById(notice);
    }

    public void deleteNotice(Long id) {
        noticeMapper.deleteById(id);
    }
}
