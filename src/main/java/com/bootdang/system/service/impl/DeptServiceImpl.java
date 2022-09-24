package com.bootdang.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Dept;
import com.bootdang.system.entity.Tree;
import com.bootdang.system.entity.User;
import com.bootdang.system.mapper.DeptMapper;
import com.bootdang.system.mapper.UserMapper;
import com.bootdang.system.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdang.util.BuildTree;
import com.bootdang.util.BuildTreeTwo;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    private Long[] pDepts;

    @Autowired
    UserMapper userMapper;

    @Autowired
    DeptMapper deptMapper;

    @Override
    public List<Tree<Dept>> getTree() {
        List<Tree<Dept>> trees = new ArrayList<Tree<Dept>>();
        List<Dept> sysDepts =list();
        for (Dept sysDept : sysDepts) {
            Tree<Dept> tree = new Tree<Dept>();
            tree.setId(sysDept.getDeptId());
            tree.setTitle(sysDept.getName());
            tree.setParentId(sysDept.getParentId().toString());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<Dept>> build = BuildTreeTwo.build(trees);
        return build;
    }

    @Override
    public Tree<Dept> getuserTree() {
        List<Tree<Dept>> trees = new ArrayList<Tree<Dept>>();
        List<Dept> depts =list();
        Long[] pDepts = deptMapper.listParentDept();
        Long[] uDepts = deptMapper.listAllDept();
        Long[] allDepts = (Long[]) ArrayUtils.addAll(pDepts, uDepts);
        for (Dept dept : depts) {
            if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
                continue;
            }
            Tree<Dept> tree = new Tree<Dept>();
            tree.setId(dept.getDeptId());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        for (User user : users) {
            Tree<Dept> tree = new Tree<Dept>();
            tree.setId(user.getUserId());
            tree.setParentId(user.getDepaid().toString());
            tree.setText(user.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
      BuildTreeTwo.build(trees);
        return null;
    }

}
