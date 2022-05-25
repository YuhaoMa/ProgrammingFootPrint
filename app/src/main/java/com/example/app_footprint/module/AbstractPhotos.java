package com.example.app_footprint.module;

public abstract class AbstractPhotos {
    protected ShowActivityNotifier showActivityNotifier;
    public final void setShowActivityNotifier(ShowActivityNotifier showActivityNotifier){
        this.showActivityNotifier = showActivityNotifier;
    }
}
