package com.bootdang.util;

import lombok.ToString;


public enum  JfFinalUtils{
CODEONE(1),CODETWO(2),CODETHREE(3),CODEFOUR(4),CODEFIVE(5),CODESIX(6),CODESERVEN(7),CODEEIGHT(8),CODENINE(9),CODETEN(10);

        public Integer getCode () {
                return code;
        }

        public void setCode (Integer code) {
                this.code = code;
        }

        private Integer code;

        private JfFinalUtils(Integer code){
               this.code=code;
        }


}

/*public class JfFinalUtils {
        public static  final  Integer SYSTEMADDJf=5;//审核通过添加积分
}*/
