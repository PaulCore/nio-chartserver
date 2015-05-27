package com.hhjt.entities;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by paul on 2015/4/29.
 */
public class User {
    private ChannelHandlerContext context;//用户所对应的接收线程;
    private char state = '1';//用户状态：1在线，2离线，3掉线,4忙
    private byte level;//用户级别：50H客户，20H医师
    private String sessionId;//用户所在会话室编号，若不在则为null

    public User(ChannelHandlerContext context, byte level) {
        this.context = context;
        this.level = level;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public char getState() {
        return state;
    }

    public void setState(char state) {
        this.state = state;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
