package com.zhangyingwei.treehole.jekyll.tool;

import com.zhangyingwei.treehole.jekyll.tool.utils.FileUtils;

import java.util.List;

/**
 * Created by zhangyw on 2017/7/10.
 */
public class App {
    public static void main(String[] args) {
        String fromPath = "/Users/zhangyw/zhangyw/code/github.com/treehole-theme/hcz-jekyll-blog";
        String toPath = "/Users/zhangyw/zhangyw/code/github.com/treehole-theme/hcz-treehole-blog";
        new FileExecuter(fromPath, toPath).execute();
    }
}
