package com.hhjt.message;

/**
 * Created by paul on 2015/4/30.
 */
public class SessionResponseMessage extends ResponseMessage {
    private int result;
    private String errorInfo = "";
    private String sessionId = "";
    public SessionResponseMessage(int result, String sessionId){
        super(null);
        cmd = "APAS";
        this.result = result;
        this.sessionId = sessionId;
        paramLength = String.format("%04d",7);
    }
    public SessionResponseMessage(String errorInfo, int result){
        super(null);
        cmd = "APAS";
        this.result = result;
        this.errorInfo = errorInfo;
        paramLength = String.format("%04d",1 + errorInfo.length());
    }
//    public SessionResponseMessage(int result, String errorInfo){
//
//    }

    @Override
    public byte[] encode() {
        excutDataLength();
       source =  (head + dataLength + cmd + paramLength + result + errorInfo + sessionId + tail).getBytes();
        return source;
    }


}
