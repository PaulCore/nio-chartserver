package com.hhjt.message;

/**
 * Created by paul on 2015/4/28.
 */
public abstract class Message {
    protected String cmd;
    protected String paramLength;
    protected String dataLength;
    protected byte[] source;

    public Message(byte[] source) {
        this.source = source;
    }

    public String getCmd() {
        return cmd;
    }

    public byte[] getSource() {
        return source;
    }
}
