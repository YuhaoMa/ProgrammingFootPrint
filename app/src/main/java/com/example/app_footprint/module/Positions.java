package com.example.app_footprint.module;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Positions extends AbstractPositions{
    private String userId;
    private Location upLoadLocation;
    private String email;
    private String groupName;
    private List<Position> myPositions;
    private Map<String,String> groupMap;
    private String userName;
    public Positions(String email,String userId,Map<String,String> groupMap,String userName) {
        super();
        this.email = email;
        this.groupName = null;
        this.userId = userId;
        myPositions = new ArrayList<>();
        upLoadLocation = null;
        this.groupMap = groupMap;
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Position> getMyPositions() {
        return myPositions;
    }

    public void setMyPositions(List<Position> myPositions) {
        this.myPositions = myPositions;
        mapsActivityNotifier.setMarker(myPositions);
    }

    public Map<String, String> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(Map<String, String> groupMap) {
        this.groupMap = groupMap;
        mapsActivityNotifier.refresh();
    }

    public String getGroupId()
    {
        return groupMap.get(groupName);
    }

    public void addGroupMap(String name, String id)
    {
        groupMap.put(name,id);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
