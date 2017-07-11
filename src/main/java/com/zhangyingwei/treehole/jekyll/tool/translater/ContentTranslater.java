package com.zhangyingwei.treehole.jekyll.tool.translater;

import com.zhangyingwei.treehole.jekyll.tool.utils.GolobCache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangyw on 2017/7/10.
 */
public class ContentTranslater implements Translater {

    private static final String DELIMITER_PLACEHOLDER_START = "\\${";
    private static final String DELIMITER_PLACEHOLDER_END = "}";
    private static final String DELIMITER_STATEMENT_START = "<%";
    private static final String DELIMITER_STATEMENT_END = "%>";

    private static final String JEKYLL_DELIMITER_PLACEHOLDER_START = "\\{\\{";
    private static final String JEKYLL_DELIMITER_PLACEHOLDER_END = "}}";
    private static final String JEKYLL_DELIMITER_STATEMENT_START = "\\{%";
    private static final String JEKYLL_DELIMITER_STATEMENT_END = "%}";

    @Override
    public String exec(String name, String content) {
        content = content
                .replaceAll(JEKYLL_DELIMITER_PLACEHOLDER_START, DELIMITER_PLACEHOLDER_START)
                .replaceAll(JEKYLL_DELIMITER_PLACEHOLDER_END,DELIMITER_PLACEHOLDER_END)
                .replaceAll(JEKYLL_DELIMITER_STATEMENT_START,DELIMITER_STATEMENT_START)
                .replaceAll(JEKYLL_DELIMITER_STATEMENT_END,DELIMITER_STATEMENT_END);
        content = this.forExec(content);
        content = this.ifExec(content);
        content = this.includeExec(content);
        content = this.actionExec(content);
//        content = this.contentExec(name,content);
        return content;
    }

    /**
     * 这里暂时就这样吧
     * 不管用
     * @param name
     * @param content
     * @return
     */
    private String contentExec(String name, String content) {
        if(content.startsWith("---\nlayout:") && !content.contains("permalink")){
            Pattern pattern = Pattern.compile("(layout:)(.+)");
            Matcher matcher = pattern.matcher(content);
            if(matcher.find()){
//                String result = matcher.group().trim().replaceAll(" ","").replace(":","/")+".html";
                GolobCache.cache("content", content);
            }
        }
        if(content.contains("{ content }")){
            System.out.println("content -> replace "+name);
            System.out.println(GolobCache.get("content"));
            content.replaceAll("\\$\\{ content \\}", GolobCache.get("content"));
        }
        return content;
    }

    private String ifExec(String content) {
        content = content.replaceAll(" if ", " if(");
        content = content.replaceAll("endif", " } ");
        content = replaceNext(content, "%>", " if(", "){ ");
        content = content.replaceAll("else", "} else {");
        content = content.replaceAll("elsif", "} else if( ");
        content = replaceNext(content, "%>", "} else if(", "){ ");
        return content;
    }

    private String forExec(String content){
        content = content.replaceAll(" for ", " for( ");
        content = content.replaceAll("endfor", " } ");
        content = replaceNext(content, "%>", " for(", "){ ");
        return content;
    }

    private String includeExec(String content){
        System.out.println("----include----");
        content = content.replaceAll("include ", "include(\"");
        content = replaceNext(content, "%>", "include(", "\",{}){}");
        return content;
    }

    private String actionExec(String content){
        System.out.println("----action----");
        Pattern pattern = Pattern.compile("(?<=\\$\\{)(.+?)(?=\\})");
        Matcher matcher = pattern.matcher(content);
        boolean find = matcher.find(0);
        int indexPy = 0;
        while(find){
            String result = matcher.group(0);
            int length = content.length();
            content = this.actionTranslater(content,result,matcher.start()+indexPy,matcher.end()+indexPy);
            indexPy = content.length() - length;
            find = matcher.find(matcher.end());
        }
        return content;
    }

    private String actionTranslater(String content, String result, int start, int end) {
        StringBuffer buffer = new StringBuffer(content);
        if(result.contains("|")){
            String[] results = result.replaceAll(" ", "").split("\\|");
            String cont = results[0];
            System.out.println("from: " + result);
            for (String res : results) {
                if(res.startsWith("prepend")){
                    cont = this.prependAction(cont,res);
                }else if(res.startsWith("replace")){
                    cont = this.replaceAction(cont,res);
                }else if(res.startsWith("date_to_string")){
                    cont = this.date2StringAction(cont,res);
                }
            }
            System.out.println("to: " + cont);
            buffer = buffer.replace(start, end, cont);
        }else{
            System.out.println(result.trim() + " 没有调用方法");
        }
        return buffer.toString();
    }

    private String date2StringAction(String cont, String res) {
        return res + "(" + cont + ")";
    }

    private String replaceAction(String cont, String res) {
        String[] items = res.replace("replace:","").split(",");
        String content = cont;
        String from = items[0];
        String to = items[1];
        return "replace(" + content + "," + from + "," + to + ")";
    }

    private String prependAction(String cont, String item) {
        return item.split(":")[1] + " + " + cont;
    }

    private String replaceNext(String content, String next, String from, String to) {
        StringBuffer buffer = new StringBuffer(content);
        int index = buffer.indexOf(from);
        while(index != -1){
            int index2 = buffer.indexOf(next, index+1);
            if(index2 != -1){
                buffer = buffer.replace(index2 - 1, index2, to);
            }
            index = buffer.indexOf(from,index+1);
        }
        content = buffer.toString();
        return content;
    }

    public static void main(String[] args) {
        String content = "layout: default\nasdfasdf";
        Pattern pattern = Pattern.compile("(layout:)(.+)");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            String result = matcher.group();
            System.out.println(result);
        }
    }
}
