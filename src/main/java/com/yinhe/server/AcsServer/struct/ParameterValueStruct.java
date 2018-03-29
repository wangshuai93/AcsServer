package com.yinhe.server.AcsServer.struct;

import java.io.Serializable;

/**
* inform - event - ParameterValueStruct中的
*/
public class ParameterValueStruct <T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2078509439430104517L;
	public static final String Type_String = "string";
    public static final String Type_Int = "int";
    public static final String Type_UnsignedInt = "unsignedInt";
    public static final String Type_Boolean = "boolean";
    public static final String Type_Base64 = "base64";
    public static final String Type_Object = "object";
    public static final String Type_Any = "any";
    public static final String Type_DateTime = "dateTime";
    private String name;
    private T value;		// Type: any
    private String valueType;									// 数据的类型
    private boolean readWrite;									// 是否是可读写的，默认是只读
    
    /**
     * name 和 value 是需要同步填充的
     * @param name
     * @param value
     */
    public ParameterValueStruct(String name, T value, String type)
    {
        this.name = name;
        this.value = value;
        this.valueType = type;
        this.readWrite = false;
    }

    /**
     * 无参数构造函数，使用时，需要同步初始化name和value
     */
    public ParameterValueStruct()
    {
        this.name = "";
        this.valueType = Type_String;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public boolean isReadWrite() {
		return readWrite;
	}

	public void setReadWrite(boolean readWrite) {
		this.readWrite = readWrite;
	}
    
	@Override
    public String toString()
    {
        StringBuilder sbd = new StringBuilder();
        sbd.append("Name:" + name);
        sbd.append(",Value:" + String.valueOf(value != null ? value : ""));
        return sbd.toString();
    }
}
