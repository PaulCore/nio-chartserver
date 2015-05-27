package com.hhjt.message;

/**
 * Created by paul on 2015/4/29.
 */
public class ConnectResponseMessage extends ResponseMessage {
    private int result;
    private String errorInfo = "";
    public ConnectResponseMessage(int result) {
        super(null);
        cmd = "COAS";
        this.result = result;
        paramLength = String.format("%04d",1);
    }
    public ConnectResponseMessage(int result, String errorInfo) {
        this(result);
        this.errorInfo = errorInfo;
        paramLength = String.format("%04d", 1 + errorInfo.length());
    }



    @Override
    public byte[] encode() {
        excutDataLength();
        source =(head + dataLength + cmd + paramLength + result + errorInfo + tail).getBytes();
        return source;
    }
}
