package com.itexcelsior.mbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


import com.google.gson.Gson;
import com.itexcelsior.mbluetooth.callback.ConnectBlueCallBack;
import com.itexcelsior.mbluetooth.connect.ConnectBlueTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description:
 * @author: zxw
 * @date: 2020/5/27 08:59
 */
public class BluetoothSocketUtils {

    private static String TAG=BluetoothSocketUtils.class.getName();

    /**
     * 蓝牙适配器
     **/
    private static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    /**
     * 连接 （在配对之后调用）
     *
     * @param device
     */
    public static void connect(BluetoothDevice device, ConnectBlueCallBack callBack) {
        if (device == null) {
            Log.d(TAG, "bond device null");
            return;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //连接之前把扫描关闭
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        new ConnectBlueTask(callBack).execute(device);
    }


    /**
     * 判断蓝牙是否打开
     *
     * @return
     */
    public static boolean isBlueEnable() {
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }

    public static boolean isSupportBlue() {
        return mBluetoothAdapter != null;
    }


    /**
     * 与蓝牙服务端进行信息交互
     *
     * @param socket
     * @param requestSocketInfo 请求参数，表明意图
     * @return
     */
    public static String exchangeWithServer(BluetoothSocket socket, SocketInfo requestSocketInfo) {

        /**
         * 1、开启写入数据线程，激活服务端返回数据方法
         * 2、开启读取数据线程，循环等待，知道读取到信息为止
         * 3、关闭线程
         */
        String str = new Gson().toJson(requestSocketInfo);


        /**
         * 模拟第一次连接握手
         */
        InputStream inputStream = null;
        String rspStr = null;//从蓝牙服务端获取到的结果
        try {
            OutputStream os = socket.getOutputStream();
            os.write(str.getBytes("UTF-8"));
            //开启读取数据线程，直到成功读取数据成功后才返回对应数据
            inputStream = socket.getInputStream();
            Log.i("@@@@@@@@ITEMCLICK-收到数据:", "streamCount====" + inputStream.available());
            while (true) {
                if (inputStream.available() > 0) {
                    byte[] buf = new byte[inputStream.available()];
                    inputStream.read(buf);
                    rspStr = new String(buf);
                    Log.i("@@@@@@@@ITEMCLICK-收到数据:", rspStr);
                    break;
                } else {
                    Log.i("@@@@@@@ITEMCLICK-未收到数据:", "streamCount====XXXXXXXXXX" + inputStream.available());
                    Thread.sleep(1000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return rspStr;
        }

    }
}
