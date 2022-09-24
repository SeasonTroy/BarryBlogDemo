package com.bootdang.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdang.system.entity.Menu;
import com.bootdang.system.mapper.MenuMapper;
import com.bootdang.system.service.IMenuService;
import com.bootdang.util.BuildTree;
import com.bootdang.util.TreeMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    MenuMapper menuMapper;

    @Override
    public List<TreeMenu> selectMenuAllMy(){
        QueryWrapper<Menu> querywrapper = new QueryWrapper();
        querywrapper.eq("state","1").orderByAsc("order_num");
        List<Menu> menus = menuMapper.selectList(querywrapper);
        LinkedList<TreeMenu> trees = new LinkedList<>();
        for(Menu menu:menus){
              TreeMenu treeMenu = new TreeMenu();
              treeMenu.setTitle(menu.getTitle());
              treeMenu.setId(menu.getMenuId());
              treeMenu.setParentid(menu.getParentid());
              treeMenu.setHref(menu.getUrl());
              treeMenu.setIcon(menu.getIcon());
              trees.add(treeMenu);
          }
        List<TreeMenu> build = BuildTree.build(trees);
        return build;
    }

    @Transactional
    @Override
    public List<Menu> selectMenuAll () {
        return menuMapper.selectMenuAll();
    }


}
