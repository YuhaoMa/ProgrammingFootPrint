package com.example.app_footprint;

import org.json.JSONArray;

import java.util.Date;

public interface Api {
    public final static String basicURL = "";
    public JSONArray logIn(String user, String password);
    public void register(String name, String password, String email);
    public JSONArray gallery(String groupID);
    public JSONArray getRoutine(Date date, int period);

}
