package com.itexcelsior.mbluetooth.connect;

import android.bluetooth.BluetoothDevice;

import com.itexcelsior.mbluetooth.callback.ConnectBlueCallBack;

/**
 * @description: 蓝牙连接
 * @author: zxw
 * @date: 2020/6/30 10:16
 */
public interface IConnect {
    void connect(BluetoothDevice bluetoothDevice, ConnectBlueCallBack connectBlueCallBack);
}
