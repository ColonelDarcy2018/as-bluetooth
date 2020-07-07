package com.itexcelsior.mbluetooth.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.itexcelsior.mbluetooth.SocketConstants;
import com.itexcelsior.mbluetooth.callback.PinBlueCallBack;


/**
 * 配对广播接收类
 * @author zxw
 */
public class PinBlueReceiver extends BroadcastReceiver {
    private String pin = SocketConstants.BLUETOOTH_PAIR_PASSWORD;  //此处为你要连接的蓝牙设备的初始密钥，一般为1234或0000
    private static final String TAG = PinBlueReceiver.class.getName();
    private PinBlueCallBack callBack;

    public PinBlueReceiver(PinBlueCallBack callBack){
        this.callBack = callBack;
    }

    //广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)){
            try {
                callBack.onBondRequest();
                //1.确认配对
//                ClsUtils.setPairingConfirmation(device.getClass(), device, true);//实测反射报错
//                Method setPairingConfirmation = device.getClass().getDeclaredMethod("setPairingConfirmation",boolean.class);
//                setPairingConfirmation.invoke(device,false);
                //2.终止有序广播
//                Log.d("order...", "isOrderedBroadcast:"+isOrderedBroadcast()+",isInitialStickyBroadcast:"+isInitialStickyBroadcast());
//                abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                //3.调用setPin方法进行配对...
//                ClsUtils.setPin(device.getClass(), device, pin);
//                Method setPin = device.getClass().getDeclaredMethod("setPin", new Class[]{byte[].class});
//                Boolean returnValue = (Boolean) setPin.invoke(device, new Object[]{pin.getBytes()});


                /**
                 * pin模式下自动配对
                 */
                int type = -1;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT,BluetoothDevice.ERROR);
                    Log.d(TAG, "ACTION_PAIRING_REQUEST--"+type);
                    //PAIRING_VARIANT_PIN 0
                    if(type == BluetoothDevice.PAIRING_VARIANT_PIN){
                        //防止弹出一闪而过的配对框
                        abortBroadcast();
                        //弹框后自动输入密码、自动确定
                        boolean isSetPin = device.setPin("0000".getBytes());
                        Log.d(TAG, "setPin()-->" + isSetPin);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_NONE:
                    Log.d(TAG, "取消配对");
                    callBack.onBondFail(device);
                    break;
                case BluetoothDevice.BOND_BONDING:
                    Log.d(TAG, "配对中");
                    callBack.onBonding(device);
                    break;
                case BluetoothDevice.BOND_BONDED:
                    Log.d(TAG, "配对成功");
                    callBack.onBondSuccess(device);
                    break;
            }
        }
    }
}
