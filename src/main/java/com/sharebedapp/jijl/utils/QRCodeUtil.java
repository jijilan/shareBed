package com.sharebedapp.jijl.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: jijl
 * @Description: 二维码生成
 * @Date: 2018/8/3 16:51
 **/
public class QRCodeUtil {
    /**
     * 生成二维码方法
     *
     * @param equipmentNumber  唯一编码
     * @param resp response对象
     * @throws Exception 抛出异常
     */
    public void getQrcode(String equipmentNumber,Integer equipmenLockType,String bluetoothName, HttpServletResponse resp) throws Exception {
        ServletOutputStream stream = null;
        try {
            stream = resp.getOutputStream();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<String,Object> map=new HashMap<>();
            map.put("equipmentNumber",equipmentNumber);
            map.put("equipmenLockType",equipmenLockType);
            map.put("bluetoothName",bluetoothName);
            String json = JsonUtils.objectToJson(map);
            BitMatrix bm = qrCodeWriter.encode(json, BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToStream(bm, "png", stream);
        } catch (WriterException e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }

}
