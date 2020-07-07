package com.itexcelsior.mbluetooth.connect;

import android.bluetooth.BluetoothDevice;

import com.itexcelsior.mbluetooth.BluetoothSocketUtils;
import com.itexcelsior.mbluetooth.callback.ConnectBlueCallBack;

/**
 * @description:
 * @author: zxw
 * @date: 2020/6/30 10:20
 */
public class Connect implements IConnect {
    @Override
    public void connect(BluetoothDevice bluetoothDevice, ConnectBlueCallBack connectBlueCallBack) {
        BluetoothSocketUtils.connect(bluetoothDevice,connectBlueCallBack);
    }
}
