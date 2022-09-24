package com.bootdang.system.service;

import com.bootdang.system.entity.Power;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface IPowerService extends IService<Power> {
      public List<Power> MySelectByAllMenu();
      public List<Power> selectByUserId(Integer id);
}
