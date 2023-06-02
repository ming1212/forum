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

    /**
     * 实体类型：帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型：评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型：用户
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 主题： 评论
     */
    String TOPIE_COMMENT = "comment";

    /**
     * 主题： 点赞
     */
    String TOPIE_LIKE = "like";

    /**
     * 主题： 关注
     */
    String TOPIE_FOLLOW = "follow";

    /**
     * 系统用户的ID
     */
    int SYSTEM_USER_ID = 1;
}
