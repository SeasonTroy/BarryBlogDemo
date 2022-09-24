package com.bootdang.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.upload")
public class FileAdd {

    public  String path;

    public String luceneIndexPath;

    public  String username;

    public  String password;
}
