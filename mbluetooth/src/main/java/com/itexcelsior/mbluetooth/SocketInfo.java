package com.itexcelsior.mbluetooth;

/**
 * 蓝牙通讯信息规范
 */
public class SocketInfo {



    public SocketInfo() {
    }

    public SocketInfo(int code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private int code;

    private String msg;

    private  String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
