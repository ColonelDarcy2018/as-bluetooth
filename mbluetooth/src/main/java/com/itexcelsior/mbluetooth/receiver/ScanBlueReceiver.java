package com.itexcelsior.mbluetooth.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.itexcelsior.mbluetooth.callback.ScanBlueCallBack;

/**
 *扫描广播接收类
 * @author zxw
 */
public class ScanBlueReceiver extends BroadcastReceiver {
    private static final String TAG = ScanBlueReceiver.class.getName();
    private ScanBlueCallBack callBack;

    public ScanBlueReceiver(ScanBlueCallBack callBack){
        this.callBack = callBack;
    }

    //广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (action){
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                Log.d(TAG, "开始扫描...");
                callBack.onScanStarted();
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                Log.d(TAG, "结束扫描...");
                callBack.onScanFinished();
                break;
            case BluetoothDevice.ACTION_FOUND:
                Log.d(TAG, "发现设备...");
                callBack.onScanning(device);
                break;
        }
    }
}
