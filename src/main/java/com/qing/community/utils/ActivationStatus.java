package com.qing.community.utils;

public interface ActivationStatus {
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

}
