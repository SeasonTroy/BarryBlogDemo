package com.bootdang.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bootdang.system.entity.Log;
import com.bootdang.system.mapper.LogMapper;
import com.bootdang.system.service.ILogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

}
