package com.badtudou.tudou;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by badtudou on 2017/4/4.
 */

public class Rest extends Thread {
    private static final int  ERROR_CODE = -1;
    private static final int  SUCCESS_CODE = 0;
    private Handler handler;
    private Message message;
    private String urlPath, host, path;
    private Map<String, String> parameters, propertys;
    private String method;
    private int time_out;

    Rest(Handler handler){
        this.handler = handler;
        this.message = new Message();
    }


    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public void setTimeout(int time_out){
        this.time_out = time_out;
    }

    public void post(String url, Map<String, String> params) throws IOException{
        this.urlPath = url;
        this.parameters = params;
        this.method = "POST";
    }

    @Override
    public void run() {
        System.out.println("开始");
        super.run();

        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.urlPath);
            connection = (HttpURLConnection) url.openConnection();
            InputStream inStream  = connection.getInputStream();
            if (connection.getResponseCode() == 200){
                message.what = SUCCESS_CODE;
            }
            System.out.println("开始读取数据");
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            String resultString = outStream.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
            outStream.close();
            System.out.println("结果"+resultString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException connecExcep){
             message.what = ERROR_CODE;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
            this.handler.sendMessage(this.message);
        }
    }
}
