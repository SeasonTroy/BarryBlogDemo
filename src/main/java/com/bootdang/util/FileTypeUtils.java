package com.bootdang.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

public class FileTypeUtils {

    public static int typeName(String name){
        if(name==null||name.equals("")){
             return 404;
        }
        String typename=name.substring(name.lastIndexOf(".")+1,name.length()).toLowerCase();
        String[] img = { "bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
                "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf" };
        for(int i=0;i<img.length;i++){
            if(img[i].equals(typename)){
               return 0;
            }
        }
// 创建文档类型数组1
        String[] document = { "txt", "doc", "docx", "xls", "htm", "html", "jsp", "rtf", "wpd", "pdf", "ppt" };
        for (int i = 0; i < document.length; i++) {
            if (document[i].equals(typename)) {
                return 1;
            }
        }
        // 创建视频类型数组2
        String[] video = { "mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm" };
        for (int i = 0; i < video.length; i++) {
            if (video[i].equals(typename)) {
                return 2;
            }
        }
        // 创建音乐类型数组3
        String[] music = { "mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg",
                "m4a", "vqf" };
        for (int i = 0; i < music.length; i++) {
            if (music[i].equals(typename)) {
                return 3;
            }
        }

        return 99;

    }

    public static String uploadAddress(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String upload = request.getSession().getServletContext().getRealPath("upload/");
        return upload;
    }



}
