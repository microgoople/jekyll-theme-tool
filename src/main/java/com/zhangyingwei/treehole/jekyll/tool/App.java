package com.zhangyingwei.treehole.jekyll.tool;

import com.zhangyingwei.treehole.jekyll.tool.utils.FileUtils;

import java.util.List;

/**
 * Created by zhangyw on 2017/7/10.
 */
public class App {
    public static void main(String[] args) {
        String fromPath = "D:/work/code/zhangyingwei/treehole-jekyll/theme-docs/hcz-jekyll-blog";
        String toPath = "D:/work/code/zhangyingwei/treehole-jekyll/theme-docs/hcz-treehole-blog";
        new FileExecuter(fromPath, toPath).execute();
    }
}
