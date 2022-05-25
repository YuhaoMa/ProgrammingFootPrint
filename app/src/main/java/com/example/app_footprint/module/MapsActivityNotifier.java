package com.example.app_footprint.module;

import java.util.List;

public interface MapsActivityNotifier {
    void setMarker(List<Position> positionList);
    void refresh();
}
