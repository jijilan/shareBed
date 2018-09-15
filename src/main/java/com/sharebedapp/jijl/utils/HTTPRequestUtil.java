package com.sharebedapp.jijl.utils;

import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by shuige on 2017/11/2.
 */
public class HTTPRequestUtil {
    public static JSONObject doPost(String url,Map jsonParam){
        JSONObject objDate=new JSONObject();
        DefaultHttpClient httpClient=new DefaultHttpClient();
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
        httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,10000);
        HttpPost httpPost=new HttpPost(url);
        String result;
        try{
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> iterator = jsonParam.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            StatusLine statusLine = response.getStatusLine();
            System.out.println("GXEJ interface request status:"+statusLine.getStatusCode());
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {//链接成功
                //通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理。
                result = EntityUtils.toString(response.getEntity());
                if(result==null|| "".equals(result)){
                    objDate.put("status",false);
                    return objDate;
                }
                objDate= JSONObject.fromObject(result);
            }else{
                System.out.println("request is error!——————POST");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("http request is error ！——————POST");
        }
        httpPost.releaseConnection();
        return objDate;
    }

    public static JSONObject doGet(String url){
        JSONObject objDate=new JSONObject();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,5000);
        String strResult="";
        try{
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                objDate = JSONObject.fromObject(strResult);
            }else{
                System.out.println("request is error!——————GET");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("http request is error ！——————GET");
        }
        return objDate;
    }

    public static String getTokenStr(JSONObject obj){
        String paramValueStr="";
        for (Iterator iter = obj.keys(); iter.hasNext(); ) { //先遍历整个 people 对象
            String key = (String) iter.next();
            if("".equals(paramValueStr))
                paramValueStr=paramValueStr+"?"+key + "=" + obj.getString(key);
            else
                paramValueStr=paramValueStr+"&"+key + "=" + obj.getString(key);
        }
        return paramValueStr;
    }

    public static String getPramValueStr(JSONObject obj) {
        String paramValueStr="";
        for (Iterator iter = obj.keys(); iter.hasNext(); ) { //先遍历整个 people 对象
            String key = (String) iter.next();
            if("".equals(paramValueStr))
                paramValueStr=paramValueStr+"?{\""+key + "\":" + obj.getString(key);
            else
                paramValueStr=paramValueStr+",\""+key + "\":" + obj.getString(key);
        }
        return paramValueStr+"}";
    }

    public static String getReturnData(String urlString) {
        String res = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),
                            "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
