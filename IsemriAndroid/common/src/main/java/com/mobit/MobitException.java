package com.mobit;

import java.util.ArrayList;
import java.util.List;

public class MobitException extends Exception {

	protected int code = 0;
	protected String caption = s_caption;
	protected List<Object> list = null;
	
	private static final long serialVersionUID = 2399653853022297784L;
	public static final String s_caption = "Hata";
	
	public int getCode()
	{
		return code;
	}
	public String getCaption()
	{
		return caption;
	}
	
	public MobitException() {
		super();
	}
	
	private static String msg(int code, String message){
		if(code == 0) return message;
		MsgInfo info = MsgInfo.findMsgInfo(code);
		return String.format("%s\n%s",info != null ? info.getMessage() : "", message);
	}
	
	public MobitException(int code, String message, String caption, Throwable cause) {
		super(msg(code, message), cause);
		this.code = code;
		this.caption = caption;
		
	}
	
	public MobitException(String message, String caption, Throwable cause) {
		this(0, message, caption, cause);	
	}

	public MobitException(int errorCode, String message) {
		this(errorCode, message, s_caption);
	}
	public MobitException(int errorCode) {
		this(errorCode, "", s_caption);
	}
	public MobitException(String message) {
		this(message, s_caption);
	}
	
	public MobitException(int errorCode, String message, String caption) {
		this(errorCode, message, caption, null);
		
	}
	public MobitException(String message, String caption) {
		this(0, message, caption);
		
	}

	public MobitException(int errorCode, String message, Throwable cause) {
		this(errorCode, message, s_caption, cause);
	}
	public MobitException(String message, Throwable cause) {
		this(0, message, s_caption, cause);
	}
	
	public MobitException(Throwable cause) {
		this(0, "", s_caption, cause);	
	}

	protected MobitException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

		super(msg(code, message), cause, enableSuppression, writableStackTrace);
		this.code = code;
	}
	protected MobitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

		this(0, message, cause, enableSuppression, writableStackTrace);
	}

	public boolean isSuccessful()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		if(list == null) return getMessage();
		StringBuilder sb = new StringBuilder();
		if(getMessage() != null) sb.append(String.format("%s\n\n", getMessage()));
		for(Object obj : list){
			String s = null;
			if(obj != null && (s = obj.toString()) != null)
				sb.append(String.format("%s\n\n", s));
		}
		return sb.toString();
	}
	
	public void add(Object obj)
	{
		if(list == null) list = new ArrayList<Object>();
		list.add(obj);
	}

}
