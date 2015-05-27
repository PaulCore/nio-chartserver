package com.hhjt.message;

/**
 * Created by paul on 2015/5/12.
 */
public class StateMessage extends ResponseMessage {
    public StateMessage(byte[] source) {
        super(source);
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
