package com.example.app_footprint.module;

import com.example.app_footprint.Email.GenerateCode;
import com.example.app_footprint.Email.SendMailUtil;

import java.util.HashMap;
import java.util.Map;

public class UserModel extends AbstractPositions{

    private String password;
    private String sendCode;
    public UserModel() {
        groupMap = new HashMap<>();
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
        SendMailUtil.send(email, sendCode, 3, null);
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
    @Override
    public void setGroupMap(Map<String, String> groupMap) {
        this.groupMap = groupMap;
        mainActivityNotifier.parsePositions();
    }
}
