package com.mobit;

import com.mobit.DialogResult;

public enum DialogResult {

	None(0),
	Ok(1),
	Yes(2),
	No(3),
	Cancel(4);
	
	private final int value;
	
	DialogResult(final int value)
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
    	case 0: return "Belirsiz";
        case 1: return "Tamam";
        case 2: return "Evet";
        case 3: return "Hayır";
        case 4: return "İptal";
        }
    	return null;
    }
    
    public static DialogResult fromInteger(int x) {
        switch(x) {
        case 0: return None;
        case 1: return Ok;
        case 2: return Yes;
        case 3: return No;
        case 4: return Cancel;
        }
        return null;
    }
}
