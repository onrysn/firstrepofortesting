package com.mobit;

import com.mobit.IPlatform;
import com.mobit.ModLoader;

public class ModLoader {

	public static final String AndroidPlatform = "mobit.android.AndroidPlatform";
	
	
	static ClassLoader classLoader = ModLoader.class.getClassLoader();
	
	public static IPlatform loadPlatform() throws Exception
	{
		return (IPlatform)load(AndroidPlatform);
	}
	
	public static Object load(String className) throws Exception
	{
		Object obj = null;
		Class<?> c = null;
		try {
			c = classLoader.loadClass(className);
			obj = c.newInstance();
		}
		catch(Exception e){
			
			throw new MobitException(String.format("%s modülü yüklenemedi", className), e);
			
		}
		return obj;
	}
	
}
