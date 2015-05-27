package com.hhjt.message;

/**
 * Created by paul on 2015/4/29.
 */
public class ConnectRequestMessage extends RequestMessage {
    private byte level;
    public ConnectRequestMessage(byte[] source) {
        super(source);
        this.cmd = "CORQ";
        this.paramLength = "0012";
        decode();
    }

    @Override
    public void decode() {
        registerNumber = new String(source,14,11);
        level = source[25];
    }

    public byte getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return super.toString() + " level=" +level;
    }
}
