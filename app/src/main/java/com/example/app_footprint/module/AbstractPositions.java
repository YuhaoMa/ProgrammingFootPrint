package com.example.app_footprint.module;

import com.example.app_footprint.MapsActivity;

public abstract class AbstractPositions {
    protected MapsActivityNotifier mapsActivityNotifier;
    public final void setMapsActivityNotifier(MapsActivityNotifier mapsActivityNotifier){
        this.mapsActivityNotifier = mapsActivityNotifier;
    }
}
