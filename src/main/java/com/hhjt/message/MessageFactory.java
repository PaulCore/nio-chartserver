package com.hhjt.message;

import com.hhjt.utils.CommandEnum;

/**
 * Created by paul on 2015/4/29.
 */
public class MessageFactory {
    public static Message getMessage(String command, byte[] source){
        CommandEnum cmd = CommandEnum.valueOf(command);
        switch (cmd){
            case CORQ:
                return new ConnectRequestMessage(source);
            case APRQ:
                return new SessionRequestMessage(source);
            case JOAS:
                return new SessionConfirmResponse(source);
            case SEND:
                return new ContentRequestMessage(source);
            case TEST:
                return new Test(source);
            default: return null;
        }
    }
}
