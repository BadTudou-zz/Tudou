package com.badtudou.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by badtudou on 2017/4/4.
 */

public class Rest extends Thread {
    public static final int  SUCCESS_CODE = 0;
    public static final int ERROR_MalformedURL = 1;
    public static final  int ERROR_Socket = 2;
    public static final  int ERROR_Server = 3;
    public static final  int ERROR_Io = 4;
    public static final String RESULT_STRING = "resultString";
    public static final String ACTION = "action";
    public static final String MSG = "msg";
    public static final String STATE = "state";
    public static final String STATE_CODE = "stateCode";
    private Handler handler;
    private Message message;
    private String urlPath, host, path;
    private Map<String, String> parameters, propertys;
    private String parametersString, method;
    private int time_out;

    public Rest(Handler handler){
        this.handler = handler;
        this.message = new Message();
    }


    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public void setTimeout(int time_out){
        this.time_out = time_out;
    }

    public void get(String url, Map<String, String> params) throws IOException{
        this.urlPath = url;
        this.parameters = params;
        this.method = "GET";
    }

    public void post(String url, Map<String, String> params) throws IOException{
        this.urlPath = url;
        this.parameters = params;
        this.method = "POST";
        this.parametersString = "";
        Iterator<Map.Entry<String, String>> integer = this.parameters.entrySet().iterator();
        while (integer.hasNext()) {
            Map.Entry<String, String> entry = integer.next();
            String key = entry.getKey();
            String value = entry.getValue();
            this.parametersString += (key + "=" + value + "&");
        }
        this.parametersString = this.parametersString.substring(0, this.parametersString.length()-1);
    }

    @Override
    public void run() {
        super.run();

        HttpURLConnection connection = null;
        try {
            // set connect infos
            URL url = new URL(this.urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(this.method);

            // get output stream
            OutputStream outStream = connection.getOutputStream();

            // send data
            outStream.write(this.parametersString.getBytes());//post的参数 xx=xx&yy=yy

            // get input stream
            InputStream inStream  = connection.getInputStream();

            // get connect state code
            if (connection.getResponseCode() == 200){
                message.what = SUCCESS_CODE;
            }
            else{
                message.what = ERROR_Server;
            }

            // get connect response info
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                byteStream.write(buffer, 0, len);
            }

            // close stream
            inStream.close();
            outStream.close();

            // translate result
            Bundle bundle = new Bundle();
            bundle.putString(RESULT_STRING, byteStream.toString());
            bundle.putString(STATE_CODE, String.valueOf(connection.getResponseCode()));
            message.setData(bundle);

        } catch (MalformedURLException e) {
            message.what = ERROR_MalformedURL;
        } catch (SocketException socketExcep){
            message.what = ERROR_Socket;
        } catch (IOException ioExcep){
            message.what = ERROR_Io;
        }
        finally {
            connection.disconnect();
            this.handler.sendMessage(this.message);
        }
    }
}
