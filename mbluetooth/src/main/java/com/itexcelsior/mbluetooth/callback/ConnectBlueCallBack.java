package com.itexcelsior.mbluetooth.callback;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * 蓝牙连接线程回掉函数
 */
public interface ConnectBlueCallBack {


    void onStartConnect();

    void onConnectSuccess(BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket);

    void onConnectFail(BluetoothDevice bluetoothDevice, String message);
}
