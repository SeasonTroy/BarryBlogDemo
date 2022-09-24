package com.bootdang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdang.system.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {
          public List<Menu> selectMenuAll();

          @Select("select*from sys_menu where menu_id=#{id}")
          public Menu mySelectById(@Param("id") Integer id);
}
