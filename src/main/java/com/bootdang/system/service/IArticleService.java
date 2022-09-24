package com.bootdang.system.service;

import com.bootdang.system.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 */
public interface IArticleService extends IService<Article> {
    public List<Article> selectArticleAll(Article article);
    public String upload(MultipartFile multipartFile);
    public Map<String,Object> insert(Article article) throws IOException;
    public boolean delete(Integer id) throws IOException;
    public Map<String,Object> indexAll();
    public Boolean deleteall(String[] ids);
    public Boolean deleteIndexAll();
    public Boolean shall(String[] ids);
    public Map<String,Object> update(Article article);
    public Model indexModel(Model model);
    public int updateCount(Integer id);
}
