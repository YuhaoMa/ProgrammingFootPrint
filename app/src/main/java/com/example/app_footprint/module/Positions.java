package com.example.app_footprint.module;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class Positions extends AbstractPositions{
    private String userId;
    private Location upLoadLocation;
    private String email;
    private String groupName;
    private List<Position> myPositions;

    public Positions(String email, String groupName,String userId) {
        this.email = email;
        this.groupName = groupName;
        this.userId = userId;
        myPositions = new ArrayList<>();
        upLoadLocation = null;
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
}
