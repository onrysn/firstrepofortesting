package com.mobit;

public abstract class Enum {

	private final Object value;
	private final Object baseValue;
	
	protected Enum(final Object value, final Object baseValue)
	{
		this.value = value;
		this.baseValue = baseValue;
		
	}
		
	public Object getDerivedValue() {
        return value;
    }
	public Object getBaseValue() {
        return baseValue;
    }
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null) return false;
		if(this == obj) return true;
		
		Object _obj;
		_obj = getBaseValue();
		while(_obj != null){
			if(_obj == obj) return true;
			if(!(_obj instanceof Enum))break;
			Enum en = (Enum)_obj;
			_obj = en.getBaseValue();
		}
		
		if(!(obj instanceof Enum)) return false;
		_obj = ((Enum)obj).getBaseValue();
		while(_obj != null){
			if(_obj == this) return true;
			if(!(_obj instanceof Enum))break;
			Enum en = (Enum)_obj;
			_obj = en.getBaseValue();
		}
		
		return false;
		
	}

}
