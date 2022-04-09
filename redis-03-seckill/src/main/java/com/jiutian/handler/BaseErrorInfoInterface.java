package com.jiutian.handler;

/**
 * ClassName:BaseErrorInfoInterface
 * Package:com.jiutian.handler
 * Description:
 *
 * @Date: 2021/12/11 11:19
 * @Author: jiutian
 */
public interface BaseErrorInfoInterface {
    /**
     * 错误码
     */
    String getResultCode();

    /**
     * 错误描述
     */
    String getResultMsg();
}
