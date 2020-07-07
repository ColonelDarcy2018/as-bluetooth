package com.itexcelsior.mbluetooth.pin;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;

import com.itexcelsior.mbluetooth.callback.PinBlueCallBack;
import com.itexcelsior.mbluetooth.callback.ScanBlueCallBack;

/**
 * @description: 蓝牙配对接口，将蓝牙配对方法内聚
 * @author: zxw
 * @date: 2020/6/29 09:34
 */
public interface IPin {

    /**
     * 配对
     * @param device
     * @param pinBlueCallBack
     * @param context
     */
    void pin(BluetoothDevice device, PinBlueCallBack pinBlueCallBack, Context context);

    /**
     * 取消配对
     * @param device
     */
    void cancelPinBule(BluetoothDevice device);


    /**
     * 打开蓝牙，开启扫描 （通过广播返回扫描到的设备信息）
     * @param scanBlueCallBack
     * @param context
     */
    void openBluetooth(ScanBlueCallBack scanBlueCallBack, Context context);
}
