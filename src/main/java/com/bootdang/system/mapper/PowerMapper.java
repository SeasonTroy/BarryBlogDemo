package com.bootdang.system.mapper;

import com.bootdang.system.entity.Power;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 */
public interface PowerMapper extends BaseMapper<Power> {

    public List<Power> selectAllMenu();

    public List<Power> selectByUserId(Integer id);

}
