package com.itexcelsior.mbluetooth.pin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;

import com.itexcelsior.mbluetooth.ClsUtils;
import com.itexcelsior.mbluetooth.callback.PinBlueCallBack;
import com.itexcelsior.mbluetooth.callback.ScanBlueCallBack;
import com.itexcelsior.mbluetooth.receiver.PinBlueReceiver;
import com.itexcelsior.mbluetooth.receiver.ScanBlueReceiver;

import java.lang.reflect.Method;

/**
 * @description: 蓝牙配对
 * @author: zxw
 * @date: 2020/6/28 15:38
 */
public class Pin implements IPin {

    private final String TAG="Pin";

    /**
     * 蓝牙适配器
     **/
    private BluetoothAdapter mBluetoothAdapter;


    public Pin() {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    /**
     * 配对（配对成功与失败通过广播返回）
     *
     * @param device
     */
    @Override
    public void pin(BluetoothDevice device, PinBlueCallBack pinBlueCallBack, Context context) {
        if (device == null) {
            Log.e(TAG, "bond device null");
            return;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //配对之前把扫描关闭
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //判断设备是否配对，没有配对在配，配对了就不需要配了
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to bond:" + device.getName());
            try {
                //这里只需要createBond就可以了
                ClsUtils.createBond(device.getClass(), device);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "attemp to bond fail!");
            }
        }

        //注册广播监听配对结果
        PinBlueReceiver pinBlueReceiver = new PinBlueReceiver(pinBlueCallBack);
        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);//配对中
        IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//配对状态改变
        context.registerReceiver(pinBlueReceiver, filter4);
        context.registerReceiver(pinBlueReceiver, filter5);
    }

    /**
     * 取消配对（取消配对成功与失败通过广播返回 也就是配对失败）
     *
     * @param device
     */
    @Override
    public void cancelPinBule(BluetoothDevice device) {
        if (device == null) {
            Log.d(TAG, "cancel bond device null");
            return;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //判断设备是否配对，没有配对就不用取消了
        if (device.getBondState() != BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to cancel bond:" + device.getName());
            try {
                Method removeBondMethod = device.getClass().getMethod("removeBond");
                Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
                returnValue.booleanValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "attemp to cancel bond fail!");
            }
        }
    }



    /**
     * 打开蓝牙并开始搜索设备
     *
     */
    @Override
    public void openBluetooth(ScanBlueCallBack scanBlueCallBack, Context context) {
        if (!isSupportBlue()) return;

        openBlueAsync();

        scanBlue();

        /**
         * 注册广播接收设备扫描结果
         */
        //用bluetoothReceiver来获取结果
        final ScanBlueReceiver scanBlueReceiver = new ScanBlueReceiver(scanBlueCallBack);
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_FOUND);//注册广播接收信号

        context.registerReceiver(scanBlueReceiver, filter1);
        context.registerReceiver(scanBlueReceiver, filter2);
        context.registerReceiver(scanBlueReceiver, filter3);

        //解除注册
        //        unregisterReceiver(scanBlueReceiver);
    }



    /**
     * 扫描周围设备
     *
     * @return
     */
    private boolean scanBlue() {
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return false;
        }
        //当前是否在扫描，如果是就取消当前的扫描，重新扫描
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //此方法是个异步操作，一般搜索12秒
        return mBluetoothAdapter.startDiscovery();
    }


    /**
     * 取消扫描蓝牙
     *
     * @return true 为取消成功
     */
    public boolean cancelScanBule() {
        if (isSupportBlue()) {
            return mBluetoothAdapter.cancelDiscovery();
        }
        return true;
    }



    public boolean isSupportBlue() {
        return mBluetoothAdapter != null;
    }


    /**
     * 判断蓝牙是否打开
     *
     * @return
     */
    public boolean isBlueEnable() {
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }


    /**
     * 异步开启蓝牙，不提示
     */
    public void openBlueAsync() {
        if (isSupportBlue()) {
            mBluetoothAdapter.enable();
        }
    }



    //    /**
//     * 蓝牙是否连接
//     * @return
//     */
//    public boolean isConnectBlue(){
//        return mBluetoothSocket != null && mBluetoothSocket.isConnected();
//    }
//
//    /**
//     * 断开连接
//     * @return
//     */
//    public boolean cancelConnect(){
//        if (mBluetoothSocket != null && mBluetoothSocket.isConnected()){
//            try {
//                mBluetoothSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        mBluetoothSocket = null;
//        return true;
//    }


//    /**
//     * MAC地址连接
//     * 输入mac地址进行自动连接
//     * 前提是系统保存了该地址的对象
//     * @param address
//     * @param callBack
//     */
//    public void connectMAC(String address, ConnectBlueCallBack callBack) {
//        if (!isBlueEnable()){
//            return ;
//        }
//        BluetoothDevice btDev = mBluetoothAdapter.getRemoteDevice(address);
//        connect(btDev, callBack);
//    }

}
