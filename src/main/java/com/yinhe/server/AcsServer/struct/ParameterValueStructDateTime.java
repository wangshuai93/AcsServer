package com.yinhe.server.AcsServer.struct;


public final class ParameterValueStructDateTime extends ParameterValueStruct<String>
{
    private static final long serialVersionUID = 8383736136511239623L;

    /**
     * 表示为UTC（全球统一时间）
     * 2天3小时4分5秒将表示为0000-00-02T03:04:05
     * @param name
     * @param value
     */
    public ParameterValueStructDateTime(String name, String value)
    {
        super(name, value, Type_DateTime);
    }
}
