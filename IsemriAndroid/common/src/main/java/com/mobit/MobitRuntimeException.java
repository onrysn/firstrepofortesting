package com.mobit;

public class MobitRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int errorCode = 0;
	protected String caption = s_caption;
	
	public static final String s_caption = "Hata";
	
	public int getErrorCode()
	{
		return errorCode;
	}
	public String getCaption()
	{
		return caption;
	}
	
	public MobitRuntimeException() {
		super();
	}
	
	private static String msg(int errorCode, String message){
		if(errorCode == 0) return message;
		MsgInfo info = MsgInfo.findMsgInfo(errorCode);
		return String.format("%s\n%s",info != null ? info.getMessage() : "", message);
	}
	
	public MobitRuntimeException(int errorCode, String message, String caption, Throwable cause) {
		super(msg(errorCode, message), cause);
		this.errorCode = errorCode;
		this.caption = caption;
		
	}
	
	public MobitRuntimeException(String message, String caption, Throwable cause) {
		this(0, message, caption, cause);	
	}

	public MobitRuntimeException(int errorCode, String message) {
		this(errorCode, message, s_caption);
	}
	public MobitRuntimeException(int errorCode) {
		this(errorCode, "", s_caption);
	}
	public MobitRuntimeException(String message) {
		this(message, s_caption);
	}
	
	public MobitRuntimeException(int errorCode, String message, String caption) {
		this(errorCode, message, caption, null);
		
	}
	public MobitRuntimeException(String message, String caption) {
		this(0, message, caption);
		
	}

	public MobitRuntimeException(int errorCode, String message, Throwable cause) {
		this(errorCode, message, s_caption, cause);
	}
	public MobitRuntimeException(String message, Throwable cause) {
		this(0, message, s_caption, cause);
	}
	
	public MobitRuntimeException(Throwable cause) {
		this(0, "", s_caption, cause);	
	}

	protected MobitRuntimeException(int errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

		super(msg(errorCode, message), cause, enableSuppression, writableStackTrace);
		this.errorCode = errorCode;
	}
	protected MobitRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

		this(0, message, cause, enableSuppression, writableStackTrace);
	}

}
