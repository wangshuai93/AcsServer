package com.yinhe.server.AcsServer.struct;

public final class ParameterValueStructInt extends ParameterValueStruct<Integer>
{
    private static final long serialVersionUID = 4753580287517867419L;

    public ParameterValueStructInt(String name, Integer value)
    {
        super(name, value, Type_Int);
    }
}
