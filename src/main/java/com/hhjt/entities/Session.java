package com.hhjt.entities;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Created by paul on 2015/4/30.
 */
public class Session {
    private Set<String> members;
    private Map<String,Set<String>> sessionQueue = new Hashtable<String, Set<String>>();
//    private Session

//    public String getSessionId() {
//        return sessionId;
//    }
//
//    public void setSessionId(String sessionId) {
//        this.sessionId = sessionId;
//    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }
}
