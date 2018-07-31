package com.michael.Com6510.photogallary.net;

import android.content.Context;
import android.util.Log;

import com.facebook.stetho.server.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
 // This class was created using the codes from https://developerandroidguide.blogspot.co.uk/2017/05/upload-image-using-okhttp.html  on the 11/1 /2018
/**
 * Created by Michael Oshinaike on 12/01/2018.
 */

public class MultipartRequest {

    public Context context;
    public MultipartBody.Builder mBuilder;
    public OkHttpClient okHttpClient;
    public String strResponse;

    public MultipartRequest(Context context) {
        this.context = context;
        this.mBuilder = new MultipartBody.Builder();
        this.mBuilder.setType(MultipartBody.FORM);
        this.okHttpClient = new OkHttpClient();
    }

    //Add String
    public void addString(String name, String value){
        this.mBuilder.addFormDataPart(name,value);
    }

    //Add Image File
    public void addFile(String name,String filePath,String fileName){
        this.mBuilder.addFormDataPart(name,fileName, RequestBody.create(MediaType.parse("image/jpeg"),new File(filePath)));
    }

    //Execute Url
    public String execute(String url) {
        RequestBody requestBody = null;
        Request request = null;
        Response response = null;
        int code = 200;
        strResponse = null;

        try {
            requestBody = this.mBuilder.build();

            //Authenication Key here.
            request = new Request.Builder().header("Key", "Value").url(url).post(requestBody).build();
            Log.v("====Request====", "" + request);
            response = okHttpClient.newCall(request).execute();
            Log.v("=======RESPONSE=====", "" + response);

            if (!response.isSuccessful())
                throw new IOException();
            code = response.networkResponse().code();
            /*
            *Successful response from server
             */
            if (response.isSuccessful()) {
                strResponse = response.body().string() + "Sent Upload";
            }

            /*
            *Invalid URL or Server not available, please try again;
             */
            else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                strResponse = "invalid Url or Server not available,please try again";
            }
            /*
             *Connection timeout, please try again
             */
            else if (code == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
                strResponse = "Invalid Url or Server  is not responding, please try  again";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            requestBody = null;
            request = null;
            response = null;
            mBuilder = null;
            if(okHttpClient !=null) {
                okHttpClient = null;
            }
        }
    return  strResponse;}

}
