package com.zhangyingwei.treehole.jekyll.tool.translater;

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
    public String exec(String content) {
        content = content
                .replaceAll(JEKYLL_DELIMITER_PLACEHOLDER_START, DELIMITER_PLACEHOLDER_START)
                .replaceAll(JEKYLL_DELIMITER_PLACEHOLDER_END,DELIMITER_PLACEHOLDER_END)
                .replaceAll(JEKYLL_DELIMITER_STATEMENT_START,DELIMITER_STATEMENT_START)
                .replaceAll(JEKYLL_DELIMITER_STATEMENT_END,DELIMITER_STATEMENT_END);
        content = this.forExec(content);
        content = this.ifExec(content);
        content = this.includeExec(content);
        return content;
    }

    private String ifExec(String content) {
        content = content.replaceAll(" if ", " if(");
        content = content.replaceAll("endif", " } ");
        content = replaceNext(content, "%>", " if(", "){ ");
        content.replaceAll("else", "} else {");
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
}
