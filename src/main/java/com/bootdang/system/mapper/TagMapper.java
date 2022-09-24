package com.bootdang.system.mapper;

import com.bootdang.system.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 */
public interface TagMapper extends BaseMapper<Tag> {

    @Select(" select*from sys_tag t left join sys_Article_tag a on t.tag_id=a.tag_id where article_id=${id}")
    public List<Tag> selectByArticleId(@Param("id") Integer id);
}
