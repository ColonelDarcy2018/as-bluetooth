package com.itexcelsior.mbluetooth.callback;

import android.bluetooth.BluetoothDevice;

public interface ScanBlueCallBack {
    void onScanStarted();

    void onScanFinished();

    void onScanning(BluetoothDevice device);
}
