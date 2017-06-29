package com.bt.pja.common.result;

/**
 * Created by chenjiapeng on 2015/6/27 0027.
 */
public class ResultFactory {
    private static final Result<Object> SUCCESS = new Result<>(ResultCodes.SUCCESS);
    private static final Result<Object> ERROR = new Result<Object>(ResultCodes.ERROR);

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> build(int code, T data) {
        return new Result<>(code, data);
    }

    public static <T> Result<T> build(int code) {
        return new Result<>(code);
    }

    public static <T> Result<T> build(ResultCode resultCode) {
        return new Result<>(resultCode);
    }
    public static <T> Result<T> build(Result<?> otherTypeResult) {
    	return new Result<>(new ResultCode(otherTypeResult.getCode(), otherTypeResult.getDetail()));
    }

    /**
     * code是com.baitian.fourb.common.result.ResultCodes#ERROR的code
     */
    public static <T> Result<T> error(String detail) {
        return new Result<>(ERROR.getCode(), detail, null);
    }
    public static <T> Result<T> error(int code,String detail) {
    	return new Result<>(code, detail, null);
    }

    @SuppressWarnings("unchecked")
	public static <T> Result<T> error() {
        return (Result<T>) ERROR;
    }

    @SuppressWarnings("unchecked")
	public static <T> Result<T> success() {
        return (Result<T>) SUCCESS;
    }
}
