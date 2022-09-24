package com.bootdang.system.service.impl;

import com.bootdang.system.entity.Arttype;
import com.bootdang.system.mapper.ArttypeMapper;
import com.bootdang.system.service.IArttypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 资源类型表 服务实现类
 * </p>
 */
@Service
@Transactional
public class ArttypeServiceImpl extends ServiceImpl<ArttypeMapper, Arttype> implements IArttypeService {
    @Autowired
    ArttypeMapper arttypeMapper;

    public List<Arttype> selectArttype(){
        List<Arttype> list = list();
        List<Arttype> artlist = new LinkedList<>();
        list.forEach((a)->{
            if(a.getParentid()==0){
                artlist.add(a);
                list.forEach((b)->{
                    if(a.getActtypeId().equals(b.getParentid())){
                        b.setTitle("---"+b.getTitle());
                        artlist.add(b);
                    }
                });
            }
        });
        return artlist;
    }
    @Override
    public List<Arttype> selectAll(){
        List<Arttype> arttypes = arttypeMapper.selectAll();
        return arttypes;
    };
}
