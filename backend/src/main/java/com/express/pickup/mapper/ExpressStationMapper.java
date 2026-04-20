package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.express.pickup.entity.ExpressStation;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExpressStationMapper extends BaseMapper<ExpressStation> {

    /**
     * 查询所有启用的快递站
     * @return
     */
    @Select("SELECT * FROM express_station " +
            "WHERE status = 1 " +
            "ORDER BY sort_order ASC")
    List<ExpressStation> selectActiveList();
}
