package com.yuanfang.forum.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForumUtil {

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", ""); //将生成的随机字符串中的-替换成空字符串
    }

    //MD5加密
    // password + salt ---> 加密后的字符串
    public static String md5(String key) {  //key为 password + salt
        if (StringUtils.isBlank(key)) { //判断传入的参数是否为空
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 生成JSON格式的字符串
     *
     * @param code 编码
     * @param msg  消息
     * @param map  存放业务数据
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {   //重载
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {   //重载
        return getJSONString(code, null, null);
    }

//    public static void main(String[] args) {
//        String username = "xiaonian";
//        int age = 24;
//        Map<String, Object> map = new HashMap<>();
//        map.put("username","xiaonian");
//        map.put("age",24);
//        String s = getJSONString(520, "爱你", map);
//        System.out.println(s);
//    }
}