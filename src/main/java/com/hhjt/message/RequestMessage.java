package com.hhjt.message;

/**
 * Created by paul on 2015/4/29.
 */
public abstract class RequestMessage extends Message {
    protected String registerNumber;
    public RequestMessage(byte[] source) {
        super(source);
    }

    public abstract void decode();

    public String getRegisterNumber() {
        return registerNumber;
    }

    @Override
    public String toString() {
        return "registerNumber=" + registerNumber;
    }
}
