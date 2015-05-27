package com.hhjt.queues;

import com.hhjt.entities.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paul on 2015/4/29.
 */
public class OnLineUsers {
    private static OnLineUsers onlineUsers = new OnLineUsers();
    private Map<String,User> users;
    private OnLineUsers(){
        users = new HashMap<String, User>();
    }
    public static OnLineUsers getOnLineUsers(){
        return onlineUsers;
    }
    public void addUser(String registerNumber, User user){
        users.put(registerNumber,user);
    }
    public User getUser(String registerNumber){
        return users.get(registerNumber);
    }

    public boolean containUser(String registerNumber){
        return  users.containsKey(registerNumber);
    }
    public Map<String,User> getUsers(){
        return users;
    }
    public void remove(String registerNumber){
        users.remove(registerNumber);
    }

}
