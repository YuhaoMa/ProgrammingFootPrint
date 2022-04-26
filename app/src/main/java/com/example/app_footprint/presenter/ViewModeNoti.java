package com.example.app_footprint.presenter;

public interface ViewModeNoti {
    final static String basicurl = "https://studev.groept.be/api/a21pt105/";
    String makeGETRequest(String urlName);
    boolean checkUserInfo(String email, String password);
}
