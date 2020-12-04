package com.itexcelsior.mbluetooth.server;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.itexcelsior.mbluetooth.BluetoothSocketUtils;
import com.itexcelsior.mbluetooth.SocketConstants;
import com.itexcelsior.mbluetooth.SocketInfo;
import com.itexcelsior.mbluetooth.callback.BluetoothSocketServerCallback;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothSocketServerThread extends Thread {

    private static final String TAG = BluetoothSocketServerThread.class.getName();
    private BluetoothServerSocket mmServerSocket=null;
    private static BluetoothSocket socket = null;
    private BluetoothSocketServerCallback mCallback;

    public BluetoothSocketServerThread(BluetoothSocketServerCallback callback) {
        mCallback=callback;
    }

    /**
     * This method usually used in callback method #{BluetoothSocketServerCallback}
     * @param str
     */
    public static void sendBluetoothMessage(String str) {
        OutputStream os = null;
        try {
            if (socket==null){
                Log.e(TAG, "socket is null,please get BluetoothSocket first");
                return;
            }
            os = socket.getOutputStream();
            //将参数数据传输到客户端

            os.write(str.getBytes("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        super.run();

        /**
         * 开启蓝牙服务器
         */
        try {
            mmServerSocket = BluetoothSocketUtils.getBluetoothServerSocket();
            Log.i(TAG, "onCreate: 开启蓝牙服务器成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: 开启蓝牙服务器失败");
        }

        while (true) {
            try {
                BluetoothSocketUtils.setBluetoothName("新光智能头盔");
                Log.i(TAG, "onCreate: 蓝牙服务端等待别人连接,MAC地址：" + BluetoothSocketUtils.getAddress() + ",设备名称：" + BluetoothSocketUtils.getBluetoothName());
                socket = mmServerSocket.accept();//此方法阻塞线程
                if (socket != null) {
                    /**
                     * 直到接收到信息之后才返回信息
                     */
                    InputStream inputStream = socket.getInputStream();

                    // 将字节输入流转化成字符输入流，并设置编码格式，InputStreamReader为 Reader 的子类
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    String rspStr = null;

                    Log.i(SocketConstants.DEBUG_TAG, "streamCount====" + inputStream.available());
                    while (true) {
                        if (inputStream.available() > 0) {
                            byte[] buf = new byte[inputStream.available()];

                            inputStream.read(buf);

                            rspStr = new String(buf, "utf-8");

                            Log.i(SocketConstants.DEBUG_TAG, rspStr);
                            break;
                        } else {
                            Log.i(SocketConstants.DEBUG_TAG, "streamCount====XXXXXXXXXX" + inputStream.available());
                            Thread.sleep(100);
                        }
                    }

                    SocketInfo socketInfo = JSONObject.parseObject(rspStr, SocketInfo.class);
                    Log.i(TAG, "onCreate: 蓝牙服务器收到信息" + socketInfo.getMsg());
                    //回调函数
                    if (mCallback!=null){
                        mCallback.onReceive(socketInfo);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
