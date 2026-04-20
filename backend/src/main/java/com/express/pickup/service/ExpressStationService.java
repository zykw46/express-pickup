package com.express.pickup.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.entity.ExpressStation;
import com.express.pickup.mapper.ExpressStationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 快递站服务业务
 */
@Service
@RequiredArgsConstructor
public class ExpressStationService extends ServiceImpl<ExpressStationMapper, ExpressStation> {

    private final ExpressStationMapper stationMapper;

    public List<ExpressStation> getActiveList() {
        return stationMapper.selectActiveList();
    }

    public List<ExpressStation> getAllList() {
        LambdaQueryWrapper<ExpressStation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ExpressStation::getSortOrder);
        return stationMapper.selectList(wrapper);
    }

    public ExpressStation getStationById(Long id) {
        return stationMapper.selectById(id);
    }

    public void addStation(ExpressStation station) {
        stationMapper.insert(station);
    }

    public void updateStation(ExpressStation station) {
        stationMapper.updateById(station);
    }

    public void deleteStation(Long id) {
        stationMapper.deleteById(id);
    }
}
