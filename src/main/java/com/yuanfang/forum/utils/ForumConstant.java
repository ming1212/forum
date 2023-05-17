package com.yuanfang.forum.utils;


public interface ForumConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEATED = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_UNSUCCESS = 2;

    /**
     * 凭证默认的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态下的超时时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 60;

}
