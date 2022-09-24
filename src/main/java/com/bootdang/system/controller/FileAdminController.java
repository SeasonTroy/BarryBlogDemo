package com.bootdang.system.controller;

import com.bootdang.system.entity.FileEntity;
import com.bootdang.system.entity.User;
import com.bootdang.system.mapper.FileMapper;
import com.bootdang.system.service.FileService;
import com.bootdang.util.HttpResult;
import com.bootdang.util.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件管理控制器
 */
@Controller
@RequestMapping("/admin/fileadmin")
public class FileAdminController {

    @Autowired
    FileService fileService;

    /**
     * 文件管理页面
     * @return
     */
    @RequiresPermissions(value = "admin-fileadmin")
    @RequestMapping("")
    public String fileadmin(){
        return "admin/page/fileadmin/file";
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @RequiresPermissions(value = "admin-fileadmin-insert")
    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Map<String,Object> fileimage(@RequestParam("file") MultipartFile file){
        int upload = fileService.upload(file);
        HashMap<String,Object> r= new HashMap<>();
        if(upload==0){
            r.put("code",0);
            r.put("msg","上传失败");
        }else{
            r.put("code",1);
            r.put("msg","上传成功");
        }
        return r;
    }

    /**
     * 查询文件
     * @param limit
     * @param offset
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public  Map<String, Object> filelist(String limit,String offset,@RequestParam(required = false,defaultValue = "0" )int type){
            Map<String, Object> list = fileService.list(limit, offset, type);
        return list;
    }

    /**
     * 文件删除
     * @param id
     * @return
     */
    @RequiresPermissions(value = "admin-fileadmin-delete")
    @ResponseBody
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public  HttpResult fileremove(@RequestParam("id") Integer id){
        Integer isAdmin = ShiroUtils.getUser().getIsAdmin();

        if(isAdmin.equals(1)){
            int remove = fileService.remove(id);
            if(remove>0){
                return new HttpResult(1,"删除成功");
            }else{
                return new HttpResult(0,"删除失败");
            }
        }
          return new HttpResult(0,"你不是超级管理员没有权限");


    }

    /**
     * 文件下载
     * @param id
     * @return
     * @throws IOException
     */
    @RequiresPermissions(value = "admin-fileadmin-downolad")
    @RequestMapping(value = "/downolad")
    public ResponseEntity<InputStreamResource> filedownold(@RequestParam("id") Integer id) throws IOException {
        String selectbyid = fileService.selectbyid(id);
        FileSystemResource fileSystemResource = new FileSystemResource(selectbyid);

        HttpHeaders hash=new HttpHeaders();
        hash.add("Cache-Control","no-cache, no-store, must-revalidate");
        hash.add("Content-Disposition","attachment;filename="+fileSystemResource.getFilename());
        hash.add("Pragma","no-cache");
        hash.add("Expires","0");
        ResponseEntity<InputStreamResource> body = ResponseEntity.ok()
                .headers(hash)
                .contentLength(fileSystemResource.contentLength())
                .contentType(MediaType.parseMediaType("multipart/form-data"))
                .body(new InputStreamResource(fileSystemResource.getInputStream()));
        return body;

    }

}
