package com.bootdang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdang.system.entity.Arttype;

import java.util.List;


public interface ArttypeMapper extends BaseMapper<Arttype> {
        public  Arttype selectbyId(Integer actypeid);
        public List<Arttype> selectAll();

}
