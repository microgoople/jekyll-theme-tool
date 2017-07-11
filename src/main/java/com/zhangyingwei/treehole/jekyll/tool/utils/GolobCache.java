package com.zhangyingwei.treehole.jekyll.tool.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyw on 2017/7/11.
 */
public class GolobCache {
    public static Map<String, String> cache = new HashMap<String, String>();

    public static void cache(String key,String value){
        System.out.println("----cache----");
        cache.put(key, value);
    }

    public static String get(String key){
        return cache.get(key);
    }

    public static String delete(String key){
        return cache.remove(key);
    }
}
