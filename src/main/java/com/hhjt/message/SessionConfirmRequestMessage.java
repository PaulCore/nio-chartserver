package com.hhjt.message;

/**
 * Created by paul on 2015/4/30.
 */
public class SessionConfirmRequestMessage extends ResponseMessage {
    private String sessionId;
    private String fromNumber;
    private String toNumber;
    public SessionConfirmRequestMessage(String fromNumber, String toNumber) {
        super(null);
        cmd = "JORQ";
        paramLength = "0028";
        sessionId = "000000";
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
    }

    @Override
    public byte[] encode() {
        excutDataLength();
        source = (head + dataLength + cmd + paramLength + sessionId +fromNumber + toNumber + tail).getBytes();
        return source;
    }
}
