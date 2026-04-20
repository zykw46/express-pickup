package com.express.pickup.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.pickup.entity.PackageSpec;
import com.express.pickup.mapper.PackageSpecMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageSpecService extends ServiceImpl<PackageSpecMapper, PackageSpec> {

    private final PackageSpecMapper specMapper;

    public List<PackageSpec> getActiveList() {
        return specMapper.selectActiveList();
    }

    public List<PackageSpec> getAllList() {
        LambdaQueryWrapper<PackageSpec> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(PackageSpec::getSortOrder);
        return specMapper.selectList(wrapper);
    }

    public PackageSpec getSpecById(Long id) {
        return specMapper.selectById(id);
    }

    public void addSpec(PackageSpec spec) {
        specMapper.insert(spec);
    }

    public void updateSpec(PackageSpec spec) {
        specMapper.updateById(spec);
    }

    public void deleteSpec(Long id) {
        specMapper.deleteById(id);
    }
}
