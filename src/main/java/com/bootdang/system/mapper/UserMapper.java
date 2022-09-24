package com.bootdang.system.mapper;

import com.bootdang.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    public User SelectByName(@Param(value = "name") String name);

    public User SelectByAdminName(@Param(value = "name") String name);

    public User SelectByEmail(String email);

    public User selectByUserRole(@Param("id") Integer id);

    public List<User> selectByAllRole();

    public int insert(User user);

    public int updatelogupdate(@Param("id") Integer id);

    public int updateJfADD(@Param("id") Integer id,@Param("jf") Integer jf);



}
