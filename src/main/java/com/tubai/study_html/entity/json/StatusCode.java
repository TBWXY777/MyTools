package com.tubai.study_html.entity.json;


/**
 * 状态码
 */
public class StatusCode {
    /**
     * 操作成功
     */
    public static final int OK = 200;

    /**
     * 操作失败
     */
    public static final int ERROR = 201;

    /**
     * 用户名或密码错误
     */
    public static final int LOGIN_ERROR = 202;

    /**
     * 用户名或密码为空
     */
    public static final int ACCOUNT_PASSWORD_EMPTY = 203;

    /**
     * 没有此用户名
     */
    public static final int ACCOUNT_NOT_FONUD = 204;

    /**
     * 参数错误
     */
    public static final int PARAM_ERROR = 205;

    /**
     * 凭证过期
     */
    public static final int TOKEN_EXPIRED = 400;

    /**
     * 没有权限
     */
    public static final int UNAUTHORIZED = 401;

    /**
     *被禁止
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源不存在
     */
    public static final int NOT_FOUND = 404;

    /**
     * 业务层错误
     */
    public static final int SERVICE_ERROR = 500;

}
