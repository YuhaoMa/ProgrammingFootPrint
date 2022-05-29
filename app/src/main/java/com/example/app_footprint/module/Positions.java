package com.example.app_footprint.module;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Positions extends AbstractPositions{
    private String groupName;
    private List<Position> myPositions;
    public Positions(String email,String userId,Map<String,String> groupMap,String userName) {
        super();
        this.email = email;
        this.groupName = null;
        this.userId = userId;
        myPositions = new ArrayList<>();
        this.groupMap = groupMap;
        this.userName = userName;
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
    public String getGroupId()
    {
        return groupMap.get(groupName);
    }
    @Override
    public void setGroupMap(Map<String, String> groupMap) {
        this.groupMap = groupMap;
        mapsActivityNotifier.parsePositions();
    }
}
