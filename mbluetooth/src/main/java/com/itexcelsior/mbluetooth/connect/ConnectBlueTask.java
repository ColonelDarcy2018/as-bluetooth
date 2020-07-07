package com.itexcelsior.mbluetooth.connect;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;


import com.itexcelsior.mbluetooth.SocketConstants;
import com.itexcelsior.mbluetooth.callback.ConnectBlueCallBack;

import java.io.IOException;
import java.util.UUID;

/**
 * 蓝牙连接线程
 * @author zxw
 */
public class ConnectBlueTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {
    private static final String TAG = ConnectBlueTask.class.getName();
    private BluetoothDevice bluetoothDevice;
    private ConnectBlueCallBack callBack;

    public ConnectBlueTask(ConnectBlueCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
        bluetoothDevice = bluetoothDevices[0];
        BluetoothSocket socket = null;
        try{
            /***与服务端uuid保持一致***/
            socket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(SocketConstants.CONNECT_UUID));
            if (socket != null && !socket.isConnected()){
                socket.connect();
            }

        }catch (IOException e){
            Log.e(TAG,"socket连接失败");
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e(TAG,"socket关闭失败");
            }
        }
        return socket;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG,"开始连接");
        if (callBack != null) callBack.onStartConnect();
    }

    @Override
    protected void onPostExecute(BluetoothSocket bluetoothSocket) {
        if (bluetoothSocket != null && bluetoothSocket.isConnected()){
            Log.d(TAG,"连接成功");
            if (callBack != null) callBack.onConnectSuccess(bluetoothDevice, bluetoothSocket);
        }else {
            Log.d(TAG,"连接失败");
            if (callBack != null) callBack.onConnectFail(bluetoothDevice, "连接失败");
        }
    }
}
