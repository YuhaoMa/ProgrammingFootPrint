package com.example.app_footprint.module;

public interface MainActivityNotifier {
    void jumpToMap();
    void setWarningView(String SendCode);
    void setResetSuccessfully();
    void setErrorView();
    void parsePositions();
}
