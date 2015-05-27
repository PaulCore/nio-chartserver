package com.hhjt.queues;

import com.hhjt.entities.User;
import sun.awt.CharsetString;

import java.util.*;

/**
 * Created by paul on 2015/5/2.
 * session队列，单例
 */
public class SessionQueue {
    private Map<String,Set<String>> sessions;
    private volatile static int count = 0;
    private static SessionQueue sessionQueue = new SessionQueue();
    private SessionQueue(){
        sessions = new HashMap<String, Set<String>>();
    }
    public static SessionQueue getSessionQueue(){
        return sessionQueue;
    }
    public boolean containSession(String sessionId){
        return sessions.containsKey(sessionId);
    }

    public boolean containUser(String sessionId, String registerNumber){
        boolean preResult = containSession(sessionId);
        if (preResult){
            Set<String> users = sessions.get(sessionId);
            return users.contains(registerNumber);
        }
        return false;
    }

    /**
     * 新建session，将参与者添加到session中，并设置相应user的sessionId，state
     * @param creator
     * @param invitee
     */
    public String  createSession(String creator, String invitee){
        String sessionId = "";
        synchronized (this){
            sessionId = String.format("%06d",++count);
        }
        Set<String> members = new HashSet<String>();
        members.add(creator);
        members.add(invitee);
        User user1 = OnLineUsers.getOnLineUsers().getUser(creator);
        User user2 = OnLineUsers.getOnLineUsers().getUser(invitee);
        user1.setSessionId(sessionId);
        user2.setSessionId(sessionId);
        user1.setState('4');
        user2.setState('4');
        sessions.put(sessionId,members);
        return sessionId;
    }
    public boolean removeSession(String sessionId){
        Set<String> value = sessions.remove(sessionId);
        if (value == null)
            return false;
        OnLineUsers users = OnLineUsers.getOnLineUsers();
        for (String registerNumber: value){
            User user = users.getUser(registerNumber);
            user.setState('1');
            user.setSessionId(null);
        }
        return true;
    }

    public Set<String> getSession(String sessionId){
        return sessions.get(sessionId);
    }
}