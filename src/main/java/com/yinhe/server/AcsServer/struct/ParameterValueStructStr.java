package com.yinhe.server.AcsServer.struct;


public final class ParameterValueStructStr extends ParameterValueStruct<String>
{
    private static final long serialVersionUID = 8383736136511239687L;

    public ParameterValueStructStr(String name, String value)
    {
        super(name, value, Type_String);
    }
}
