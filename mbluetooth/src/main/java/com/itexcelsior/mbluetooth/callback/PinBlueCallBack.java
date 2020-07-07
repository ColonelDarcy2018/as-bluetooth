package com.itexcelsior.mbluetooth.callback;

import android.bluetooth.BluetoothDevice;

public interface PinBlueCallBack {


    void onBondRequest();

    void onBondFail(BluetoothDevice device);

    void onBonding(BluetoothDevice device);

    void onBondSuccess(BluetoothDevice device);
}
