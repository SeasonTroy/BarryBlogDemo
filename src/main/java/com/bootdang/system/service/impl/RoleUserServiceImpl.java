package com.bootdang.system.service.impl;

import com.bootdang.system.entity.RoleUser;
import com.bootdang.system.mapper.RoleUserMapper;
import com.bootdang.system.service.IRoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements IRoleUserService {

}
