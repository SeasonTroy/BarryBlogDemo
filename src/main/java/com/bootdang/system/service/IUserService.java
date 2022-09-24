package com.bootdang.system.service;

import com.bootdang.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface IUserService extends IService<User> {

    public User isUserByName(String name);
    public  User selectByUserRole(Integer id);

    public List<User> selectByAllRole();

    public int updatelogupdate(Integer id);
    public User SelectByAdminName(String name);

    public User selectUserByEmail(String email);

    public int updateJfADD(Integer id,Integer jf);
}
