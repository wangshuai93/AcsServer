package com.yinhe.server.AcsServer.struct;


public final class ParameterValueStructBoolean extends ParameterValueStruct<Boolean>
{
    private static final long serialVersionUID = 4401570145164802074L;

    public ParameterValueStructBoolean(String name, Boolean value)
    {
        super(name, value, Type_Boolean);
    }
}
