package com.hhjt.message;

/**
 * Created by paul on 2015/5/20.
 */
public class Test extends Message{
    public Test(byte[] source) {
        super(source);
        cmd = "TEST";
    }
}
