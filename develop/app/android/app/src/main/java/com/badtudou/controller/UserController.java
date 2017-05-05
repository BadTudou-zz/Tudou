package com.badtudou.controller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.badtudou.util.Rest;
import com.badtudou.model.ResultCallBack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by badtudou on 07/04/2017.
 */
public class UserController {

    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PHONE = "phone";
    public static final String USER_PASSWORD = "password";
    private Rest rest = null;
    private ResultCallBack callBack;

    public UserController(ResultCallBack callBack){
        this.callBack = callBack;
        this.rest = new Rest(this.msgHandler);
    }



    private Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Rest.SUCCESS_CODE:
                    //TODO: 处理
                    String resultResult = msg.getData().getString(Rest.RESULT_STRING);
                    Log.d("Test", "获取数据成功"+msg.getData().getString(Rest.RESULT_STRING));
                    callBack.handleResult(resultResult);
                    break;
                case Rest.ERROR_Socket:
                    Log.d("Test", "网络错误"+msg.getData().getString("stateCode"));
                    break;
                case Rest.ERROR_Io:
                    Log.d("Test", "Io错误"+msg.getData().getString("stateCode"));
                    break;
                case Rest.ERROR_Server:
                    Log.d("Test", "服务器错误");
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };


    public boolean register(String name, String phone, String password){
        Log.d("Test", "注册");
        Map mapParams = new HashMap();
        mapParams.put(USER_NAME, name);
        mapParams.put(USER_PHONE, phone);
        mapParams.put(USER_PASSWORD, password);

        try {
            this.rest.post("http://10.0.2.2:3000/signin", mapParams);
            this.rest.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean login(String name, String password){
        Log.d("Test", "登陆");
        Map mapParams = new HashMap();
        mapParams.put(USER_NAME, name);
        mapParams.put(USER_PASSWORD, password);

        try {
            this.rest.post("http://10.0.2.2:3000/login", mapParams);
            this.rest.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean logout(){
        return true;
    }


    private void send(){

    }

}
