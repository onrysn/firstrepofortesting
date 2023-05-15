package com.mobit;

public enum DbType {

	NULL(0),
	INTEGER(1),
	REAL(2),
	TEXT(3),
	BLOB(4),
	
	INT(10),
	TINYINT(11),
	SMALLINT(12),
	MEDIUMINT(13),
	BIGINT(14),
	UNSIGNEDBIGINT(15),
	INT2(16),
	INT8(17),	
	CHARACTER(18),
	VARCHAR(19),
	VARYING_CHARACTER(20),
	NCHAR(21),
	NATIVE_CHARACTER(22),
	NVARCHAR(23),
	CLOB(24),
	DOUBLE(25),
	DOUBLE_PRECISION(26),
	FLOAT_REAL(27),
	NUMERIC(29),
	DECIMAL(28),
	BOOLEAN(30),
	DATE(31),
	DATETIME(32);
	
	private final int value;
	
	DbType(final int value)
	{
		this.value = value;
	}
	
	public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	switch(this){
    	case NULL: return"NULL";
    	
    	case INTEGER: return "INTEGER";
    	case REAL: return "REAL";
    	case TEXT: return "TEXT";
    	case BLOB: return "BLOB";
    	
    	case INT: return "INT";
    	case TINYINT: return "TINYINT";
    	case SMALLINT: return "SMALLINT";
    	case MEDIUMINT: return "MEDIUMINT";
    	case BIGINT: return "BIGINT";
    	case UNSIGNEDBIGINT: return "UNSIGNED BIG INT";
    	case INT2: return "INT2";
    	case INT8: return "INT8";	
    	case CHARACTER: return "CHARACTER";
    	case VARCHAR: return "VARCHAR";
    	case VARYING_CHARACTER: return "VARYING CHARACTER";
    	case NCHAR: return "NCHAR";
    	case NATIVE_CHARACTER: return "NATIVE CHARACTER";
    	case NVARCHAR: return "NVARCHAR";
    	case CLOB: return "CLOB";
    	case DOUBLE: return "DOUBLE";
    	case DOUBLE_PRECISION: return "DOUBLE PRECISION";
    	case FLOAT_REAL: return "FLOAT REAL";
    	case NUMERIC: return "NUMERIC";
    	case DECIMAL: return "DECIMAL";
    	case BOOLEAN: return "BOOLEAN";
    	case DATE: return "DATE";
    	case DATETIME: return "DATETIME";
    	};

    	return "";
    }
}
