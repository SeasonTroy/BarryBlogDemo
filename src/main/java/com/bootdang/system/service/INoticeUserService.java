package com.bootdang.system.service;

import com.bootdang.system.entity.NoticeUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface INoticeUserService extends IService<NoticeUser> {


    public List<NoticeUser> selectNotUser(Integer id);
}
