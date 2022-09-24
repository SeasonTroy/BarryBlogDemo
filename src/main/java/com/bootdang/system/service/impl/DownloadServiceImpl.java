package com.bootdang.system.service.impl;

import com.bootdang.system.entity.Download;
import com.bootdang.system.mapper.DownloadMapper;
import com.bootdang.system.service.IDownloadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户下载表 服务实现类
 * </p>
 *
 */
@Service
public class DownloadServiceImpl extends ServiceImpl<DownloadMapper, Download> implements IDownloadService {

}
