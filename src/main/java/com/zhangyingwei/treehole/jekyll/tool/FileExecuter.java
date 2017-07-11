package com.zhangyingwei.treehole.jekyll.tool;

import com.zhangyingwei.treehole.jekyll.tool.translater.ContentTranslater;
import com.zhangyingwei.treehole.jekyll.tool.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by zhangyw on 2017/7/10.
 */
public class FileExecuter {
    private String fromPath;
    private String toPath;

    public FileExecuter(String fromPath, String toPath) {
        this.fromPath = fromPath;
        this.toPath = toPath;
        FileUtils.makeDirs(toPath);
    }

    public void execute(){
        List<String> files = FileUtils.listFiles(this.fromPath);
        files.stream().filter(file -> file.endsWith(".html")).map(file -> new String[]{file,FileUtils.content(file)}).map(content -> {
            String resultPath = this.pathTranslater(content[0]);
            String resultContent = this.contentTranslater(new File(content[0]).getName(),content[1]);
            return new String[]{resultPath,resultContent};
        }).forEach(result -> {
//            System.out.println(result[0]);
            FileUtils.storage(result[0],result[1]);
        });
        files.stream().filter(file -> !file.endsWith(".html")).forEach(file -> FileUtils.copy(file,this.pathTranslater(file)));
    }

    private String contentTranslater(String name, String content) {
        return new ContentTranslater().exec(name,content);
    }

    private String pathTranslater(String path) {
        return path.replace(this.fromPath, this.toPath);
    }
}
