package com.example.app_footprint.presenter;

import com.android.volley.toolbox.JsonArrayRequest;

public interface ViewModeNoti {
    final static String basicurl = "https://studev.groept.be/api/a21pt105/";
    String makeGETRequest(String urlName);
}
