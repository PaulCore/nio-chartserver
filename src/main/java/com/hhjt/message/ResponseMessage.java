package com.hhjt.message;

/**
 * Created by paul on 2015/4/29.
 */
public abstract class ResponseMessage extends Message {
    protected String head = "##";
    protected String tail = "\r\n";
//    protected String dataLength;
//    protected String cmd;
//    protected String paramLength;
    protected byte[] data;
    public ResponseMessage(byte[] source) {
        super(source);
    }

    public abstract byte[] encode();
    public void excutDataLength(){
        dataLength = String.format("%04d",Integer.parseInt(paramLength) + 8);
    }

}
