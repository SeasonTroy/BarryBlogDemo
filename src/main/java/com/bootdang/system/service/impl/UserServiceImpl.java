package com.bootdang.system.service.impl;

import com.bootdang.system.entity.User;
import com.bootdang.system.mapper.UserMapper;
import com.bootdang.system.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper usermapper;


    @Override
    public User isUserByName(String name){
        User user = usermapper.SelectByName(name);
        return user;
    }

     @Transactional
     public  User selectByUserRole(Integer id){
       return usermapper.selectByUserRole(id);
     }

    @Transactional
    @Override
    public List<User> selectByAllRole () {
        return usermapper.selectByAllRole();
    }

    @Override
    public int updatelogupdate (Integer id) {
        return usermapper.updatelogupdate(id);
    }

    @Override
    public User SelectByAdminName (String name) {
        return usermapper.SelectByAdminName(name);
    }

    @Override
    public User selectUserByEmail (String email) {
        return usermapper.SelectByEmail(email);
    }

    @Override
    public int updateJfADD (Integer id, Integer jf) {
        return usermapper.updateJfADD(id,jf);
    }


}
