package com.bootdang.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdang.system.entity.FileEntity;

import com.bootdang.system.mapper.FileMapper;
import com.bootdang.system.service.FileService;
import com.bootdang.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableConfigurationProperties(FileAdd.class)
public class FileServiceImpl extends ServiceImpl<FileMapper,FileEntity> implements FileService {
    private  static final Logger log= LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileAdd fileAdd;

    @Override
    public int upload (MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int i = FileTypeUtils.typeName(originalFilename);
        if(i==500){
            return 0;
        }

        String filetype=originalFilename.substring(originalFilename.lastIndexOf(".")+1,originalFilename.length()).toLowerCase();
         String fileproname=(new SimpDataTimeUtils("yyyy-MM").format(new Date()))+"/"+new Date().getTime()+"."+filetype;
        String filename=fileAdd.getPath()+fileproname;
        log.error(filename);
        File file1 = new File(filename);
        if(!file1.exists()){
            file1.mkdirs();
        }
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileType(i);

        fileEntity.setCreateuserid( ShiroUtils.getUserId());
        fileEntity.setFileUrl("/upload/"+fileproname);
        fileEntity.setCreatetime(LocalDateTime.now());
        fileMapper.insert(fileEntity);
        return 1;
    }



    @Override
    public Map<String, Object> list (String limit, String offset,  int type) {
        QueryWrapper<FileEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_type",type).last("limit "+offset+","+limit).orderByDesc("createtime");
        List<FileEntity> fileEntities = fileMapper.selectList(queryWrapper);
        Integer integer = fileMapper.selectCount(null);

        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("total",integer);
        objectObjectHashMap.put("rows",fileEntities);
        return objectObjectHashMap;
    }

    @Override
    public int remove (Integer id) {

        String selectbyid = selectbyid(id);
        File file = new File(selectbyid);
        if(file.isFile()) {
            boolean delete = file.delete();
            if (delete) {
                int i = fileMapper.deleteById(id);
                return i;
            }
        }else{
            int i = fileMapper.deleteById(id);
            return 1;
        }
        return 0;
    }

    @Override
    public String selectbyid (Integer id){

        FileEntity fileEntity = fileMapper.selectById(id);
        String fileUrl = fileEntity.getFileUrl();
        LocalDateTime createtime = fileEntity.getCreatetime();
        DateTimeFormatUtils dateTimeFormatUtils = new DateTimeFormatUtils("yyyy-MM");
        String filenameurl=fileAdd.getPath()+dateTimeFormatUtils.format(createtime)+"/"+fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());

         return filenameurl;
    }
}
