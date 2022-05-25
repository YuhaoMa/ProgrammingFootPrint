package com.example.app_footprint.module;

import com.example.app_footprint.Email.GenerateCode;
import com.example.app_footprint.Email.SendMailUtil;

import java.util.HashMap;
import java.util.Map;

public class UserModel extends AbstractPositions{

    private String userId;
    private String password;
    private String userName;
    private String address;
    private String sendCode;
    private Map<String,String> groupMap;

    public UserModel() {
        groupMap = new HashMap<>();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode() {
        GenerateCode generateCode = new GenerateCode(8);
        this.sendCode = generateCode.generateCode();
        SendMailUtil.send(address, sendCode, 3, null);
        mainActivityNotifier.setWarningView(sendCode);
    }

    public void notifyChangePassword(){
        mainActivityNotifier.setResetSuccessfully();
    }

    public void notifyErrorView(){
        mainActivityNotifier.setErrorView();
    }

    public void jumpToMap(){
        mainActivityNotifier.jumpToMap();
    }

    public Map<String, String> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(Map<String, String> groupMap) {
        this.groupMap = groupMap;
        mainActivityNotifier.parsePositions();
    }
}
