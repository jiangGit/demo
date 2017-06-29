package com.bt.pja.common.result;

public class ResultCodes {
    public static final ResultCode SUCCESS = new ResultCode(0, "成功");
    public static final ResultCode EXCEPTION = new ResultCode(-1, "系统错误");
    public static final ResultCode CONN_ERR = new ResultCode(-2, "连接失败");
    public static final ResultCode PARAM_EMPTY = new ResultCode(-3, "缺少参数");
    public static final ResultCode PARAM_ILLEGAL = new ResultCode(-4, "参数非法");
    public static final ResultCode ERROR = new ResultCode(-5, "操作失败");
    public static final ResultCode OPERATION_UNSUPPORTED = new ResultCode(-6, "当前版本不支持该操作");
    public static final ResultCode LOGIN_REQUIRED = new ResultCode(-7, "请先登录");
}
