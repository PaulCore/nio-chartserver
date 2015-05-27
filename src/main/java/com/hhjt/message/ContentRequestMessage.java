package com.hhjt.message;

/**
 * Created by paul on 2015/5/12.
 */
public class ContentRequestMessage extends RequestMessage {
    private byte[] content;
    private String sessionId;
    private String from;
    public ContentRequestMessage(byte[] source) {
        super(source);
        cmd = "SEND";
        decode();
    }

    @Override
    public void decode() {
        sessionId = new String(source,14,6);
        from = new String(source,20,11);
    }

    public String getFrom() {
        return from;
    }

    public String getSessionId() {
        return sessionId;
    }
}
