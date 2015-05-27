package com.hhjt.message;

/**
 * Created by paul on 2015/4/30.
 */
public class SessionRequestMessage extends RequestMessage{
    private String toNumber;
    public SessionRequestMessage(byte[] source) {
        super(source);
        cmd = "APRQ";
        paramLength = "0022";
        decode();
    }

    @Override
    public void decode() {
        registerNumber = new String(source,14,11);
        toNumber = new String(source,25,11);
    }

    public String getToNumber() {
        return toNumber;
    }
}
