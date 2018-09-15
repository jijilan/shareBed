package com.sharebedapp.jijl.utils;



import net.sf.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/1/5.
 */
@ConfigurationProperties(prefix = "gould")
@Component
public class GouldAPI {

    private static String key="5QABZ-5KACF-XYSJ3-NL3KZ-3DEZT-HKFHV";

    private static String gouldAddressCoordinate ="http://apis.map.qq.com/ws/geocoder/v1/";
    /**
     * 地址逆解析
     * @param addressDesc
     * @return
     */
    public static String addressToCoordinate(String addressDesc){
        String coordinate=null;
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("key", key);
        jsonObject.put("address",addressDesc);
        String paramStr= HTTPRequestUtil.getTokenStr(jsonObject);
        JSONObject gouldJSON=HTTPRequestUtil.doGet(gouldAddressCoordinate+paramStr);
        System.out.println("======================"+gouldJSON);
        if(gouldJSON.getString("message").contains("ok")&&Integer.valueOf(gouldJSON.get("status").toString())==0){
            JSONObject result = gouldJSON.getJSONObject("result");
            coordinate=result.getJSONObject("location").toString();
        }
        return coordinate;
    }

    private double EARTH_RADIUS = 6371.393;
    private double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个经纬度之间的距离
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }

    public static void main(String[] args) {
//        System.out.println(GouldAPI.GetDistance(22.498754,114.132811,22.489337,114.143818));
//        System.out.println(Helper.Distance(22.498754,114.132811,22.489337,114.143818));
        GouldAPI api = new GouldAPI();
        System.out.println(api.addressToCoordinate("广东省深圳市南山区泰邦大厦"));

    }
}
