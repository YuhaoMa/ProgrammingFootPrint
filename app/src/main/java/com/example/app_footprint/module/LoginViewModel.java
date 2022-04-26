package com.example.app_footprint.module;

import com.example.app_footprint.presenter.ViewModeNoti;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LoginViewModel implements ViewModeNoti {

    @Override
    public String makeGETRequest(String email) {
        BufferedReader rd = null;
        StringBuilder sb = null;
        String line = null;
        try {
            URL url = new URL(basicurl+"/"+email);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }
            conn.disconnect();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean checkUserInfo(String email, String password){
        try {
            if(email == null || password == null){
                return false;
            }
            else {
                JSONArray array = new JSONArray(makeGETRequest(email));
                String emailCheck = array.getJSONObject(0).getString("emailaddress");
                String passCheck = array.getJSONObject(0).getString("password");
                if(emailCheck == email && passCheck == password){
                    return true;
                }
                else {
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
