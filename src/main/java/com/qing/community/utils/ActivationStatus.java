package com.qing.community.utils;

public interface ActivationStatus {
    int ACTIVATIONSUCCESS = 0;
    int ACTIVATIONREPEAT = 1;
    int ACTIVATIONFAIL = 2;

    int EXPIRE_TIME = 3600 * 12;

    int REMEMBER_EXPIRE_TIME = 3600 * 12 * 7;

}
