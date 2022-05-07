package com.example.app_footprint.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.app_footprint.presenter.ViewModeNoti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class LoginViewModel {
    private static ArrayList<String> groups;
    private boolean check;

    public LoginViewModel() {
        this.check = true;
        groups = new ArrayList<>();
    }

    public static ArrayList<String> getGroups() {
        return groups;
    }

    public static void setGroups(String group) {
        groups.add(group);
    }
}
