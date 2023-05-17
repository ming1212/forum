package com.yuanfang.forum.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class); //获取logger日志对象便于记录日志

    private static final String REPLACEMENT = "***";  //用于替换敏感词的字符串

    /**
     * 初始化前缀树
     */
    //根节点
    private TrieNode rootNode = new TrieNode();



    @PostConstruct   //表示该方法在所在类初始化即构造器方法执行之后执行
    public void init(){   //根据敏感词初始化前缀树只需要初始化一次，所以定义一个初始化方法
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt"); //获取敏感词文件的字节输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));  //获取敏感词文件的字符缓冲流
        try {
            String keyword;
            while((keyword = reader.readLine()) != null) {
                //将敏感词添加到前缀树  可以定义一个方法
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败 :" + e.getMessage());
        }
    }



    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for(int i = 0; i < keyword.length(); i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);   //先看下当前节点是否有此字符的子节点
            if(subNode == null){  //当前节点没有此字符的子节点
                subNode = new TrieNode();  //初始化子节点
                tempNode.addSubNode(c,subNode);
            }
            tempNode = subNode;  //将临时节点指向子节点进入下一轮
            if(i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);  //如果是敏感词的最后一个字，那么给它打上标记
            }
        }
    }



    /**
     * 过滤敏感词的方法
     * @param text  待过滤的文本
     * @return  过滤后的文本
     */
    public String filter(String text){

        if(StringUtils.isBlank(text)){  //先判断文本是否为空
            return null;
        }
        //指针1
        TrieNode tempNode = rootNode;  //指向前缀树的根节点
        //指针2
        int begin = 0;  //指向检测文本的开始
        //指针3
        int position = 0;  //指向检测文本的开始
        StringBuilder sb = new StringBuilder();   //变长字符串记录过滤后的结果
        while(position < text.length()){
            char c = text.charAt(position);
            //跳过符号  将判断字符是否为符号抽取成一个方法
            if(isSymbol(c)){
                if(tempNode == rootNode){  //如果指针一处于根节点，将此符号记录结果，让指针2向下走一步
                    sb.append(c);
                    begin++;
                }
                position++;  //无论符号在开头或中间，指针3都向下走一步
                continue;
            }
            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                sb.append(text.charAt(begin));  //以begin开头的字符串不是敏感词
                position = ++begin; //进入下一个位置
                tempNode = rootNode; //重新指向根节点
            }else if(tempNode.isKeywordEnd()){  //发现敏感词，将begin ~ position之间的字符串替换掉
                sb.append(REPLACEMENT);
                begin = ++position;  //进入下一个位置
                tempNode = rootNode;  //重新指向根节点
            }else{
                position++;  //检查下一个字符
            }
        }
        sb.append(text.substring(begin));  //将最后一批字符记录结果\
        return sb.toString();
    }



    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);  //0x2E80 ~ 0x9FFF是东亚文字范围
    }


    /**
     * 通过内部类的方式定义一个前缀树
     */
    private class TrieNode{

        private boolean isKeywordEnd = false;   //关键词的结束标识

        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNode(Character c, TrieNode node){  //添加子节点
            subNodes.put(c,node);
        }

        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }


}
