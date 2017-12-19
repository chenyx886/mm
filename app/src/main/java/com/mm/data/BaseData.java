package com.mm.data;

/**
 * Company：苗苗
 * Class Describe：Http 请求基础类
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class BaseData<T> extends BaseResp {

    private int result;

    private String msg;

    private T data;

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseData() {
        super();
    }

    public BaseData(int result, String msg, T data) {
        this.result = result;
        this.msg = msg;
        this.data = data;
    }
}
