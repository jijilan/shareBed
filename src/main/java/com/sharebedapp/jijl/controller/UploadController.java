package com.sharebedapp.jijl.controller;

import com.sharebedapp.jijl.exception.MyException;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.utils.QRCodeUtil;
import com.sharebedapp.jijl.utils.UploadFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/8/4
 * @Description: 文件上传
 **/
@Slf4j
@RestController
public class UploadController {

    @Value("${web.portrait-path}")
    private String portraitPath;
    /**
     * @Description 文件上传
     * @Date 2018/7/11 20:33
     * @Author liangshihao
     */
    @PostMapping("/fileUpload")
    public ResultView fileUpload(@RequestParam("file") MultipartFile[] file, String fileName) throws MyException {
        return ResultView.ok(UploadFileUtil.flowUpload(file,portraitPath ,fileName));
    }

    /**
     * 生成二维码
     * @param equipmentNumber  设备唯一编号
     * @param equipmenLockType  设备锁类型 1蓝牙锁 2机械锁
     * @param bluetoothName  mac地址
     */
    @GetMapping("/downloadQr")
    public void downloadQr(String equipmentNumber ,Integer equipmenLockType,String bluetoothName,HttpServletResponse response){
        QRCodeUtil createQrcode = new QRCodeUtil();
        //生成二维码
        try {
            createQrcode.getQrcode(equipmentNumber,equipmenLockType,bluetoothName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
