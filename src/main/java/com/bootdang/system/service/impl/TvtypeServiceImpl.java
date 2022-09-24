package com.bootdang.system.service.impl;

import com.bootdang.system.entity.Tvtype;
import com.bootdang.system.mapper.TvtypeMapper;
import com.bootdang.system.service.ITvtypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class TvtypeServiceImpl extends ServiceImpl<TvtypeMapper, Tvtype> implements ITvtypeService {
   @Autowired
    TvtypeMapper tvtypeMapper;
    @Override
    public Tvtype[] selectDistinct () {
        return tvtypeMapper.selectDistinct();
    }
}
