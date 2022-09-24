package com.bootdang.system.mapper;

import com.bootdang.system.entity.NoticeUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface NoticeUserMapper extends BaseMapper<NoticeUser> {
        public List<NoticeUser> selectNotUser(Integer id);
}
