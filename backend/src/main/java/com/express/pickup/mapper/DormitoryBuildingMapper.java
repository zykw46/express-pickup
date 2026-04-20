package com.express.pickup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.express.pickup.entity.DormitoryBuilding;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DormitoryBuildingMapper extends BaseMapper<DormitoryBuilding> {

    /**
     * 获取所有启用的宿舍楼
     * @return
     */
    @Select("SELECT * FROM dormitory_building " +
            "WHERE status = 1 " +
            "ORDER BY sort_order ASC")
    List<DormitoryBuilding> selectActiveList();
}
