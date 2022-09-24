package com.bootdang.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdang.system.entity.Comment;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface CommentMapper extends BaseMapper<Comment> {

      public List<Comment> selectByArticleId(Integer id);
}
