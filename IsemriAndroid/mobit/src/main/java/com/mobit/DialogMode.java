package com.mobit;

import com.mobit.DialogMode;

public enum DialogMode {

	Ok(0),
	YesNo(1);
	
	private final int value;
	
	DialogMode(final int value)
	{
		this.value = value;
	}
	public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	switch(getValue()) {
        case 0: return "Tamam";
        case 1: return "Evet Hayır";
        }
    	return null;
    }
    
    public static DialogMode fromInteger(int x) {
        switch(x) {
        case 0: return Ok;
        case 1: return YesNo;
        }
        return null;
    }
}
