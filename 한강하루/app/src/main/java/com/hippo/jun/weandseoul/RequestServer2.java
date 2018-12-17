package com.hippo.jun.weandseoul;

import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by JUN on 2018-06-21.
 */

public class RequestServer2{

    private String domain;

    public RequestServer2(String serverDomain){
        this.domain=serverDomain;
    }

    public String request(ContentValues params) throws IOException {

        StringBuilder output = new StringBuilder(); //서버의 응답을 담는 변수

        try{
            URL url = new URL(domain);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(conn != null) {
                conn.setConnectTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                // 인코딩 정의 HTTP 방식으로 전송할때는 urlencoded 방식으로 인코딩해서 전송해야한다.
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");

            }

            String str = getPostString(params);
            OutputStream os = conn.getOutputStream();
            os.write(str.getBytes("UTF-8"));
            os.flush();
            os.close();

            int resCode = conn.getResponseCode();
            Log.e("Response Code : ", String.valueOf(resCode));
            if (resCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                String line = null;
                while(true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    output.append(line + "\n");
                }

                reader.close();
                conn.disconnect();
            }
            else{
                Log.e("RESPONSE", "Response Error");
            }

        } catch(MalformedURLException e){
            Log.e("MalformedURL","The Url is incorrect");
            e.printStackTrace();
        } catch(IOException e){
            Log.e("IOException","can't connect to the web page.");
            e.printStackTrace();
        }
        return output.toString();
    }

    private String getPostString(ContentValues map) {
        StringBuilder result = new StringBuilder();
        boolean first = true; // 첫 번째 매개변수 여부

        for (Map.Entry<String, Object> entry : map.valueSet()) {
            if (first){
                first = false;}
            else {// 첫 번째 매개변수가 아닌 경우엔 앞에 &를 붙임
                result.append("&");}

            try { // UTF-8로 주소에 키와 값을 붙임
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            } catch (UnsupportedEncodingException ue) {
                ue.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

}