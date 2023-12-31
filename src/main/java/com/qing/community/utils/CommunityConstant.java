package com.qing.community.utils;

public interface CommunityConstant {
    int ACTIVATIONSUCCESS = 0;
    int ACTIVATIONREPEAT = 1;
    int ACTIVATIONFAIL = 2;

    int EXPIRE_TIME = 3600 * 12;

    int REMEMBER_EXPIRE_TIME = 3600 * 12 * 7;

    /**
     * 实体类型: 帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 关注实体类型: 用户
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 主题: 点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 主题: 关注
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * 主题: 评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 系统用户ID
     */
    int SYSTEM_USER_ID = 1;

}
