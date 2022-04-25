package com.example.app_footprint.module;

import com.example.app_footprint.LoginContract;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class LoginModule extends BaseGet {
    private User user;

    @Override
    public List<String> LoaderInitData() {
        return null;
    }

    @Override
    public void checkUserInfo(String email, String password, LoginContract.CallBack callBack) throws JSONException {
        JSONArray array = new JSONArray(makeGETRequest(newUser));

    }
}
