package com.mobit;

import com.mobit.ILocation;

public interface ILocation {

	boolean isValid();
	
	float getAccuracy();
	double getAltitude();
	double getLatitude();
	double getLongitude();
	float getSpeed();
	long getTime();
	
	void setAccuracy(float accuracy);
	void setAltitude(double altitude);
	void setLatitude(double latitude);
	void setLongitude(double longitude);
	void setSpeed(float speed);
	void setTime(long time);
	
	float distanceTo(ILocation location);
	boolean hasAccuracy();
	boolean hasAltitude();
	
	String getProvider();
	
}
