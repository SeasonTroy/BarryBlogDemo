package com.bootdang.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class XssFileUtils {
     private static final Whitelist whitelist=Whitelist.basicWithImages();
     /** 配置过滤化参数,不对代码进行格式化 */
     private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
     static{

          whitelist.removeProtocols("img","src", "http", "https");

                  //添加不需要过滤的标签
          whitelist.addAttributes(":all", "style");

     }
     /*
     * 过滤的工具方法
     * */
     public static String clean(String content) {
          return Jsoup.clean(content, "", whitelist, outputSettings);
     }


     public static void main (String[] args) {
          System.out.println(clean("<p>11<img src=\"http://localhost:8889/static/lib/layui/images/face/30.jpg\" alt=\"[思考]\"><span>&lt;a href=\"http://www.baidu.com/a\" onclick=\"alert(\"模拟XSS攻击\");\"&gt;sss&lt;/a&gt;&lt;script&gt;alert(0);&lt;/script&gt;sss</span></p>"));
     }
}
