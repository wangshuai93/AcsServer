package com.yinhe.server.AcsServer.struct;

public final class ParameterValueStructUnsignedInt extends ParameterValueStruct<Long>
{
    private static final long serialVersionUID = -1252168441231306256L;

    public ParameterValueStructUnsignedInt(String name, Long value)
    {
        super(name, value, Type_UnsignedInt);
    }
}
