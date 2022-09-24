package com.bootdang.util;

import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyHashPassWordUtile {

    private volatile static SimpleHash simpleHash=null;
    public static String encode(String met,String val,String stal,Integer count){
        if(simpleHash==null) {
            simpleHash = new SimpleHash(met, val, stal, count);
            return simpleHash.toString();
        }
        return simpleHash.toString();

    }
}
