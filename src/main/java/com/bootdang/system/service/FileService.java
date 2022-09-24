package com.bootdang.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bootdang.system.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService extends IService<FileEntity> {

    public int upload(MultipartFile file);

    public Map<String, Object> list(String limit, String offset, int type);

    public int remove(Integer id);

    public String selectbyid(Integer id) throws IOException;

}
