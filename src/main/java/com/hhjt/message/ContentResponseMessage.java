package com.hhjt.message;

/**
 * Created by paul on 2015/5/12.
 */
public class ContentResponseMessage extends ResponseMessage {
    public ContentResponseMessage(byte[] source) {
        super(source);
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
