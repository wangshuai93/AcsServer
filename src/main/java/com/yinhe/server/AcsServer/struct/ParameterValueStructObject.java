package com.yinhe.server.AcsServer.struct;

public final class ParameterValueStructObject extends ParameterValueStruct<Object>
{
    private static final long serialVersionUID = 8230721944738939224L;

    public ParameterValueStructObject(String name, Object value)
    {
        super(name, value, Type_Object);
    }
}
