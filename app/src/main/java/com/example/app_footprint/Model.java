package com.example.app_footprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model {
    private static String name;
    private static String emailAddress;
    private ArrayList<Position> mData = new ArrayList<>();

    public ArrayList<Position> getmData() {
        return mData;
    }

    public void setmData(Position position) {
        mData.add(position);
    }

    public ArrayList<Position> findByUserId(String userId,String positionId){
        ArrayList<Position> x = new ArrayList<>();
        for(int i = 0; i < mData.size();i++){
            if(mData.get(i).getUserId().equals(userId) && mData.get(i).getPositionId().equals(positionId)){
                x.add(mData.get(i));
            }
        }
        return x;
    }
}
