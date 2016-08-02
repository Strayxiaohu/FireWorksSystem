package com.xiaohu.fireworkssystem.model;

/**
 * Created by Administrator on 2015/11/30.
 */
public class BlueToothModel {
    private String DeviceName;
    private String DeviceAddress;
    public BlueToothModel(){

    }
public BlueToothModel(String name,String address){
    DeviceName=name;
    DeviceAddress=address;
}
    public String getDeviceAddress() {
        return DeviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        DeviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }
}
