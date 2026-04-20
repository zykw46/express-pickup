package com.express.pickup.controller;

import com.express.pickup.common.Result;
import com.express.pickup.entity.DormitoryBuilding;
import com.express.pickup.service.DormitoryBuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宿舍楼管理
 */
@RestController
@RequestMapping("/building")
@RequiredArgsConstructor
public class DormitoryBuildingController {

    private final DormitoryBuildingService buildingService;

    /**
     * 获取所有宿舍楼列表
     *
     * @return 宿舍楼列表
     */
    @GetMapping("/list")
    public Result<List<DormitoryBuilding>> list() {
        List<DormitoryBuilding> list = buildingService.getAllList();
        return Result.success(list);
    }

    /**
     * 获取所有活跃的宿舍楼列表
     *
     * @return 活跃的宿舍楼列表
     */
    @GetMapping("/active-list")
    public Result<List<DormitoryBuilding>> activeList() {
        List<DormitoryBuilding> list = buildingService.getActiveList();
        return Result.success(list);
    }

    /**
     * 获取宿舍楼详情
     *
     * @param id 宿舍楼ID
     * @return 宿舍楼详情
     */
    @GetMapping("/detail/{id}")
    public Result<DormitoryBuilding> detail(@PathVariable Long id) {
        DormitoryBuilding building = buildingService.getBuildingById(id);
        return Result.success(building);
    }

    /**
     * 添加宿舍楼
     *
     * @param building 宿舍楼信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody DormitoryBuilding building) {
        buildingService.addBuilding(building);
        return Result.success("添加成功");
    }

    /**
     * 修改宿舍楼信息
     *
     * @param building 宿舍楼信息
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody DormitoryBuilding building) {
        buildingService.updateBuilding(building);
        return Result.success("修改成功");
    }

    /**
     * 删除宿舍楼
     *
     * @param id 宿舍楼ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return Result.success("删除成功");
    }
}
