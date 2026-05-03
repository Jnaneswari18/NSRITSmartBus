package com.example.smartbus.ui.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SharedLocationManager {

    private static final MutableLiveData<String> _latitude = new MutableLiveData<>("0.000000");
    private static final MutableLiveData<String> _longitude = new MutableLiveData<>("0.000000");

    public static LiveData<String> getLatitude() {
        return _latitude;
    }

    public static LiveData<String> getLongitude() {
        return _longitude;
    }

    public static void updateLocation(String lat, String lon) {
        _latitude.setValue(lat);
        _longitude.setValue(lon);
    }

    public static void resetLocation() {
        _latitude.setValue("0.000000");
        _longitude.setValue("0.000000");
    }
}
