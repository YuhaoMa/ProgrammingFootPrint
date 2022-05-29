package com.example.app_footprint.module;

import com.example.app_footprint.MainActivity;

public interface MainActivityNotifier extends parsePositions {
    void jumpToMap();
    void setWarningView(String SendCode);
    void setResetSuccessfully();
    void setErrorView();
    void parsePositions();

}
