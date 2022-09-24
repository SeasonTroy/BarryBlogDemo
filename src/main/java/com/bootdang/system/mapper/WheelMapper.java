package com.bootdang.system.mapper;

import com.bootdang.system.entity.Wheel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 轮播表 Mapper 接口
 * </p>
 */
public interface WheelMapper extends BaseMapper<Wheel> {

     public List<Wheel> selectAll();
}
