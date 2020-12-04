package com.itexcelsior.mbluetooth.callback;

import com.itexcelsior.mbluetooth.SocketInfo;

public interface BluetoothSocketServerCallback {

    void onReceive(SocketInfo socketInfo);
}
