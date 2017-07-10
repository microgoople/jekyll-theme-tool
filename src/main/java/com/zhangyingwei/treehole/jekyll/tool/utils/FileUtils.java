package com.zhangyingwei.treehole.jekyll.tool.utils;

import java.io.*;
import java.util.*;

/**
 * Created by zhangyw on 2017/7/10.
 */
public class FileUtils {
    public static List<String> listFiles(String path){
        List<String> result = new ArrayList<String>();
        listFiles(path, result);
        return result;
    }

    private static void listFiles(String path,List<String> result){
        List<String> files = Arrays.asList(new File(path).list());
        files.stream().forEach(file -> {
            String nextPath = path + "/" + file;
            File tmp = new File(nextPath);
            if(tmp.isDirectory()){
                listFiles(nextPath, result);
            }else{
                result.add(nextPath);
            }
        });
    }

    public static void makeDirs(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static String content(String file) {
        StringBuffer result = new StringBuffer();
        try {
            new BufferedReader(new FileReader(file)).lines().map(line -> line + "\n").forEach(line -> result.append(line));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void storage(String path, String content) {
        File file = new File(path);
        try {
            File parent = new File(file.getParent());
            if(!parent.exists()){
                parent.mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            new FileWriter(path).append(content).close();
            System.out.println("storage:" + path);
        } catch (IOException e) {
            System.out.println("path:"+path);
            e.printStackTrace();
        }
    }

    public static void copy(String fromFile, String toFile) {
        File to = new File(toFile);
        try {
            File parent = new File(to.getParent());
            if(!parent.exists()){
                parent.mkdirs();
            }
            FileWriter writer = new FileWriter(to);
            writer.write(content(fromFile));
            writer.close();
            System.out.println("copy:" + toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
