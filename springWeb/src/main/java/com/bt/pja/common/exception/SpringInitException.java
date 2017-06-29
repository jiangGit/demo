package com.bt.pja.common.exception;

/**
 * spring初始化exception
 * @author huangyongsheng
 *
 */
public class SpringInitException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public SpringInitException(String message) {
		if(Boolean.valueOf(System.getProperty("my.spring.debug","false"))){
			System.err.println(message);
			System.exit(0);
		}
	}
	public SpringInitException(String message, Exception e) {
		super(message, e);
		if(Boolean.valueOf(System.getProperty("my.spring.debug","false"))){
			e.printStackTrace();
			System.exit(0);
		}
	}

	
}
