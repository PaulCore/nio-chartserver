package com.hhjt.message;

/**
 * Created by paul on 2015/4/30.
 */
public class SessionConfirmResponse extends RequestMessage {
    private int result;
    private String sessionId;
    private String fromNumber;
    private String toNumber;
    private String errorInfo = "";

    public SessionConfirmResponse(byte[] source) {
        super(source);
        cmd = "JOAS";
        decode();
    }
    /*
    public SessionConfirmResponse(int result, String sessionId, String fromNumber, String toNumber) {
        super(null);
        cmd = "JOAS";
        paramLength = "0029";
        result = this.result;
        sessionId = this.sessionId;
        fromNumber = this.fromNumber;
        toNumber = this.toNumber;
    }
    public SessionConfirmResponse(int result, String sessionId, String fromNumber, String toNumber, String errorInfo) {
        this(result,sessionId,fromNumber,toNumber);
        this.errorInfo = errorInfo;
        paramLength = String.format("%04d",29 + errorInfo.length());
    }*/

    @Override
    public void decode() {
        paramLength = new String(source,10,4);
        result = source[14] - 48;
        fromNumber = new String(source,21,11);
        toNumber = new String(source,32,11);
    }

    public int getResult() {
        return result;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public String getToNumber() {
        return toNumber;
    }

    public String getErrorInfo() {
        return errorInfo;
    }
}
