package tech.washmore.easychat.common;

/**
 * @author Washmore
 * @version V1.0
 * @summary 自定义业务异常
 * @Copyright (c) 2018, Lianjia Group All Rights Reserved.
 * 区分于系统抛出的异常,方便进行不同的文本提示
 * @since 2018/3/5
 */
public class UnLoginException extends RuntimeException {

    public UnLoginException(String message) {
        super(message);
    }

    public UnLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnLoginException(Throwable cause) {
        super(cause);
    }
}
