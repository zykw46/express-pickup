package com.express.pickup.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.entity.DormitoryBuilding;
import com.express.pickup.mapper.DormitoryBuildingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 宿舍楼管理业务
 */
@Service
@RequiredArgsConstructor
public class DormitoryBuildingService extends ServiceImpl<DormitoryBuildingMapper, DormitoryBuilding> {

    private final DormitoryBuildingMapper buildingMapper;

    public List<DormitoryBuilding> getActiveList() {
        return buildingMapper.selectActiveList();
    }

    public List<DormitoryBuilding> getAllList() {
        LambdaQueryWrapper<DormitoryBuilding> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(DormitoryBuilding::getSortOrder);
        return buildingMapper.selectList(wrapper);
    }

    public DormitoryBuilding getBuildingById(Long id) {
        return buildingMapper.selectById(id);
    }

    public void addBuilding(DormitoryBuilding building) {
        buildingMapper.insert(building);
    }

    public void updateBuilding(DormitoryBuilding building) {
        buildingMapper.updateById(building);
    }

    public void deleteBuilding(Long id) {
        buildingMapper.deleteById(id);
    }
}
