package com.example.app_footprint.module;

import java.util.Map;

public abstract class AbstractPhotos {
    protected ShowActivityNotifier showActivityNotifier;
    protected Map<String,String> groupMap;

    public final void setShowActivityNotifier(ShowActivityNotifier showActivityNotifier){
        this.showActivityNotifier = showActivityNotifier;
    }

}
