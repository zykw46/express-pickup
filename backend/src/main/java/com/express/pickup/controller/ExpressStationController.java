package com.express.pickup.controller;

import com.express.pickup.common.Result;
import com.express.pickup.entity.ExpressStation;
import com.express.pickup.service.ExpressStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 快递站管理
 */
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class ExpressStationController {

    private final ExpressStationService stationService;

    /**
     * 获取所有快递站列表
     *
     * @return 快递站列表
     */
    @GetMapping("/list")
    public Result<List<ExpressStation>> list() {
        List<ExpressStation> list = stationService.getAllList();
        return Result.success(list);
    }

    /**
     * 获取所有启用的快递站列表
     *
     * @return 启用的快递站列表
     */
    @GetMapping("/active-list")
    public Result<List<ExpressStation>> activeList() {
        List<ExpressStation> list = stationService.getActiveList();
        return Result.success(list);
    }

    /**
     * 获取快递站详情
     *
     * @param id 快递站ID
     * @return 快递站详情
     */
    @GetMapping("/detail/{id}")
    public Result<ExpressStation> detail(@PathVariable Long id) {
        ExpressStation station = stationService.getStationById(id);
        return Result.success(station);
    }

    /**
     * 添加快递站
     *
     * @param station 快递站信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody ExpressStation station) {
        stationService.addStation(station);
        return Result.success("添加成功");
    }

    /**
     * 修改快递站信息
     *
     * @param station 快递站信息
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody ExpressStation station) {
        stationService.updateStation(station);
        return Result.success("修改成功");
    }

    /**
     * 删除快递站
     *
     * @param id 快递站ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        stationService.deleteStation(id);
        return Result.success("删除成功");
    }
}
