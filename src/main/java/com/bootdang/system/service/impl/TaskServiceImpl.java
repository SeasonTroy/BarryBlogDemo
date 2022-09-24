package com.bootdang.system.service.impl;

import com.bootdang.system.entity.Task;
import com.bootdang.system.mapper.TaskMapper;
import com.bootdang.system.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

}
