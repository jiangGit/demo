package com.bt.pja.common.result;

public class Result<T> extends ResultCode {

    private static final long serialVersionUID = 1064373066413371092L;

    private static final Result<?> SUCCESS = new Result<>(ResultCodes.SUCCESS);

    @SuppressWarnings("unchecked")
	public static <T> Result<T> getSuccess() {
        return (Result<T>) SUCCESS;
    }

    private T value;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getCode() {
        return code;
    }

    public T getValue() {
        return value;
    }

    public Result(int code) {
        this(code, null,null);
    }

    public Result(T value) {
        this(ResultCodes.SUCCESS, value);
    }

    public Result(int code, T value) {
        super(code);
        this.value = value;
    }

    public Result(int code, String detail, T value) {
        super(code, detail);
        this.value = value;
    }


    public Result(ResultCode resultCode) {
        super(resultCode.getCode(), resultCode.getDetail());
    }

    public Result(ResultCode rc, T value) {
        this(rc.code, value);
    }
    public FbResult toFbResult() {
		if(error()){
			return FbResult.ERROR(this);
		}
		return FbResult.SUCCESS(value);
	}


}
