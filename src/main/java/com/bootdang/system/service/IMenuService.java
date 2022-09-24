package com.bootdang.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bootdang.system.entity.Menu;
import com.bootdang.util.TreeMenu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface IMenuService extends IService<Menu> {
    public List<TreeMenu> selectMenuAllMy();

    public List<Menu> selectMenuAll();


}
