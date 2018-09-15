package com.sharebedapp.jijl.utils;

import com.sharebedapp.jijl.exception.MyException;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public class EquipmentUtil {

    //	心跳回调地址
    public static final String EQUIPMENT_RETURN_URL = "https://lizhongsharebed.jijl-sz.com/equipmentReturn";
    // 心跳回调设置地址
    public static final String EQUIPMENT_RQUEST_URL="http://120.77.72.190/ShareController/ruida_url_set";
    // 开锁关锁请求地址
    public static final String SWITCH_LOCK_URL="http://120.77.72.190/ShareController/bedswiftcontrol";
    /**
     * 硬件回调地址设置
     * @param macno 设备号（可以是一系列设备号使用逗号拼接而成）
     * @return
     */
    public static ResultView setEquipmentReturnUrl(String macno) {
        RestTemplate restTemplate = new RestTemplate();
        String sign = MD5Util.MD5Encode("jijl", "utf-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("macno", macno);
        params.add("url", EQUIPMENT_RETURN_URL);
        params.add("sign", sign);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        String json = restTemplate.postForObject(EQUIPMENT_RQUEST_URL, httpEntity, String.class);
        ResultView resultView = JsonUtils.jsonToPojo(json, ResultView.class);
        System.out.println(resultView.getMsg());
        return resultView;
    }


    public static boolean switchLock(String equipmentNumber, String swift, String orderId) {
        RestTemplate restTemplate = new RestTemplate();
        String sign = MD5Util.MD5Encode("jijl", "utf-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("macno", equipmentNumber);
        params.add("swift", swift);
        params.add("order_id", orderId);
        params.add("url", EQUIPMENT_RETURN_URL);
        params.add("sign", sign);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        String json = restTemplate.postForObject(SWITCH_LOCK_URL, httpEntity, String.class);
        log.info("开锁返回json数据{}",json);
        if (json == null || "".equals(json)){
            throw new MyException(ResultEnum.CODE_1003);
        }
        ResultView resultView = JsonUtils.jsonToPojo(json, ResultView.class);
        if (resultView.getCode() == -1){
            throw  new MyException(ResultEnum.CODE_1007);
        }
        if (resultView.getData() == null){
            throw new MyException(ResultEnum.CODE_1003);
        }
        String dataJson=JsonUtils.objectToJson(resultView.getData());
        if (dataJson == null || "".equals(dataJson) || "[]".equals(dataJson)){
            throw new MyException(ResultEnum.CODE_1003);
        }
        log.info("返回数据-----------{}",dataJson);
        Map<String,Object> map=JsonUtils.jsonToPojo(dataJson, Map.class);
        if (map.get("status").equals(ResultStatus.MACHINE_CLOUD_STATUS_SUCCESS)){
            return true;
        }else if (map.get("status").equals(ResultStatus.MACHINE_CLOUD_STATUS_FIAL)){
            throw new MyException(ResultEnum.CODE_1005);
        }else if (map.get("status").equals(ResultStatus.MACHINE_CLOUD_STATUS_ERROR)){
            throw new MyException(ResultEnum.CODE_1004);
        }
        throw new MyException(ResultEnum.CODE_2);
    }

    /**
     * 计算电量
     * @param max 最高值 12
     * @param min 最低值 11.5
     * @param num 当前值
     * @return
     */
    public static double calculatedElectricQuantity(double max,double min,double num){
        double differenceBetween=max-min;
        double y=100/differenceBetween;
        double n=min*y;
        double percentage=Math.ceil(num*y-n);
        if (percentage > 100) {
            percentage = 100;
        }else if (percentage<0){
            percentage = 0;
        }
        return percentage;
    }
    public static void main(String[] args) {
        System.out.println(switchLock("1808210000220002","1","1111"));
       // System.out.println(String.valueOf(calculatedElectricQuantity(12,11.5,11.9)));
    }
}
