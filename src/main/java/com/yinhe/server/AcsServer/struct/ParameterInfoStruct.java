package com.yinhe.server.AcsServer.struct;


public class ParameterInfoStruct
{
    private String name;
    private boolean writable;

    /**
     * name 和 value 是需要同步填充的
     * @param name
     * @param value
     */
    public ParameterInfoStruct(String name, boolean writable)
    {
        this.name = name;
        this.writable = writable;
    }

    /**
     * 无参数构造函数，使用时，需要同步初始化name和value
     */
    public ParameterInfoStruct()
    {
        this.name = "";
        this.writable = false;
    }

    /**
	 * 如果ParameterPath是“InternetGatewayDevice.LANDevice.”，应答可能列出“InternetGatewayDevice.LANDevice.1. ”<br/>
	 * 和“InternetGatewayDevice.LANDevice.2. ”，而不会列出在该层次下面的所有参数。
	 */
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isWritable()
    {
        return writable;
    }

    public void setWritable(boolean writable)
    {
        this.writable = writable;
    }

    @Override
    public String toString()
    {
        StringBuilder sbd = new StringBuilder();
        sbd.append("Name:" + name);
        sbd.append(",Writable:" + writable);
        return sbd.toString();
    }
}
