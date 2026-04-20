package com.express.pickup.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.express.pickup.common.PageQuery;
import com.express.pickup.common.Result;
import com.express.pickup.entity.Notice;
import com.express.pickup.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告管理控制器
 */

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 获取公告列表
     *
     * @param pageQuery 分页查询参数
     * @param title 公告标题
     * @param status 公告状态
     * @return Result<IPage<Notice>> 公告列表
     */
    @GetMapping("/list")
    public Result<IPage<Notice>> list(PageQuery pageQuery,
                                       @RequestParam(required = false) String title,
                                       @RequestParam(required = false) Integer status) {
        IPage<Notice> page = noticeService.getNoticePage(pageQuery, title, status);
        return Result.success(page);
    }

    /**
     * 获取最新公告列表
     *
     * @param limit 显示数量
     * @return Result<List<Notice>> 最新公告列表
     */
    @GetMapping("/active-list")
    public Result<List<Notice>> activeList(@RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<Notice> list = noticeService.getActiveList(limit);
        return Result.success(list);
    }

    /**
     * 获取公告详情
     *
     * @param id 公告ID
     * @return Result<Notice> 公告详情
     */
    @GetMapping("/detail/{id}")
    public Result<Notice> detail(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeById(id);
        return Result.success(notice);
    }

    /**
     * 添加公告
     *
     * @param notice 公告信息
     * @return Result<String> 添加结果
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody Notice notice) {
        noticeService.addNotice(notice);
        return Result.success("添加成功");
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return Result<String> 修改结果
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody Notice notice) {
        noticeService.updateNotice(notice);
        return Result.success("修改成功");
    }

    /**
     * 删除公告
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return Result.success("删除成功");
    }
}
