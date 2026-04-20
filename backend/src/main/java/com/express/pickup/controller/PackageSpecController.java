package com.express.pickup.controller;

import com.express.pickup.common.Result;
import com.express.pickup.entity.PackageSpec;
import com.express.pickup.service.PackageSpecService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 包裹规格
 */
@RestController
@RequestMapping("/spec")
@RequiredArgsConstructor
public class PackageSpecController {

    private final PackageSpecService specService;

    /**
     * 获取所有规格列表
     *
     * @return 包裹规格列表
     */
    @GetMapping("/list")
    public Result<List<PackageSpec>> list() {
        List<PackageSpec> list = specService.getAllList();
        return Result.success(list);
    }

    /**
     * 获取所有激活的规格列表
     *
     * @return 激活的包裹规格列表
     */
    @GetMapping("/active-list")
    public Result<List<PackageSpec>> activeList() {
        List<PackageSpec> list = specService.getActiveList();
        return Result.success(list);
    }

    /**
     * 获取包裹规格详情
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result<PackageSpec> detail(@PathVariable Long id) {
        PackageSpec spec = specService.getSpecById(id);
        return Result.success(spec);
    }

    /**
     * 添加包裹规格
     * @param spec
     * @return
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody PackageSpec spec) {
        specService.addSpec(spec);
        return Result.success("添加成功");
    }

    /**
     * 修改包裹规格
     * @param spec
     * @return
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody PackageSpec spec) {
        specService.updateSpec(spec);
        return Result.success("修改成功");
    }

    /**
     * 删除包裹规格
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        specService.deleteSpec(id);
        return Result.success("删除成功");
    }
}
