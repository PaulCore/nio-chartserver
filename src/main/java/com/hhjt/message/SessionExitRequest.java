package com.hhjt.message;

/**
 * Created by paul on 2015/5/12.
 */
public class SessionExitRequest extends RequestMessage {
    public SessionExitRequest(byte[] source) {
        super(source);
    }

    @Override
    public void decode() {

    }
}
