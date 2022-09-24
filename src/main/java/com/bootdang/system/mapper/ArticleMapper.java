package com.bootdang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdang.system.entity.Article;
import org.apache.ibatis.annotations.Select;


import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {
    //basemapper没有的
       public List<Article> selectArticleAll(Article article);
       public int updateCount(Integer id);
}
