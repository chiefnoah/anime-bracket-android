package com.animebracket.android.Util.beans;

import java.io.Serializable;

/**
 * Created by Noah Pederson on 1/22/2015.
 */
public class UserInfo implements Serializable {

    private int id;
    private String name;
    private boolean admin;
    private String ip;
    private Object prizes;

    public UserInfo() {} //No arg constructor. We don't need it anyway


    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getIP() {
        return ip;
    }

    public Object getPrizes() {
        return prizes;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setIP(String IP) {
        this.ip = IP;
    }

    public void setPrizes(Object prizes) {
        this.prizes = prizes;
    }
}
