package com.example.app_footprint.module;

import com.example.app_footprint.MapsActivity;

import java.util.Map;

public abstract class AbstractPositions{
    protected MapsActivityNotifier mapsActivityNotifier;
    protected MainActivityNotifier mainActivityNotifier;
    protected String email;
    protected Map<String,String> groupMap;
    protected String userName;
    protected  String userId;
    public final void setMapsActivityNotifier(MapsActivityNotifier mapsActivityNotifier){
        this.mapsActivityNotifier = mapsActivityNotifier;
    }
    public final void setMainActivityNotifier(MainActivityNotifier mainActivityNotifier) {
        this.mainActivityNotifier = mainActivityNotifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(Map<String, String> groupMap) { };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
