package cn.zdxh.assistant.utils;


import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String OkHttpGet(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//get请求
                .build();
        final Call call = okHttpClient.newCall(request);
        String json="";
        try {
            Response response = call.execute();
            json=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String OkHttpPost(String url,String json){
        //发送post请求
        OkHttpClient okHttpClient = new OkHttpClient();
        //请求体
        RequestBody requestBody =RequestBody.create(JSON, json);
        //请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        String result="";
        try {
            Response response = okHttpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
        }
        return result;
    }

    // 上傳圖片
    public static String uploadImage(String url, String imagePath) {
        String uploadResult=null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            File file = new File(imagePath);
            RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("pic", imagePath, image)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            // 把response转换成string
            uploadResult = response.body().string();


        } catch (IOException e) {
            uploadResult = e.toString();
        }
        return uploadResult;
    }
}
