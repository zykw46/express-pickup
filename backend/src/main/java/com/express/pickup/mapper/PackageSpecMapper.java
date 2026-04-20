package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.express.pickup.entity.PackageSpec;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PackageSpecMapper extends BaseMapper<PackageSpec> {

    /**
     * 查询所有有效的包裹规格列表，按排序顺序升序排列。
     * @return 有效的包裹规格列表
     */
    @Select("SELECT * FROM package_spec " +
            "WHERE status = 1 ORDER BY sort_order ASC")
    List<PackageSpec> selectActiveList();
}
