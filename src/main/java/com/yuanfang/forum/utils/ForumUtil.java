package com.yuanfang.forum.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class ForumUtil {

    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-",""); //将生成的随机字符串中的-替换成空字符串
    }

    //MD5加密
    // password + salt ---> 加密后的字符串
    public static String md5(String key){  //key为 password + salt
        if(StringUtils.isBlank(key)){ //判断传入的参数是否为空
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
