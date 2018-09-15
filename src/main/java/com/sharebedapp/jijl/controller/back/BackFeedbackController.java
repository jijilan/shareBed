package com.sharebedapp.jijl.controller.back;


import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/back")
public class BackFeedbackController {


    private final WxFeedbackService wxFeedbackService;

    @Autowired
    public BackFeedbackController(WxFeedbackService wxFeedbackService) {
        this.wxFeedbackService = wxFeedbackService;
    }

    /***
     * 反馈列表
     *  @param feedbackType 意见类型【1.设备问题 2.支付问题 3.其他问题】
     *  @param nickName 用户昵称
     *  @param  status 1.未处理 2.已处理
     * @param  startTime 开始时间
     * @param  endTime 结束时间
     * @param pageNo 页数
     * @param pageSize 页数大小

     * @return
     */
    @GetMapping(value = "/getFeedbackList")
    public ResultView getFeedbackList(Integer feedbackType,String nickName, Integer status, String startTime ,String endTime, @RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo,
                                       @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
              if(feedbackType==null){
                  return  ResultView.error(ResultEnum.CODE_155);
             }
              if(feedbackType!=1&&feedbackType!=2&&feedbackType!=3){
                return  ResultView.error(ResultEnum.CODE_156);
             }
        return wxFeedbackService.getFeedbackList(feedbackType,nickName,status,startTime,endTime,pageNo,pageSize);
    }

    /***
     * 反馈详情
     *  @param feedbackId 意见编号
     * @return
     */
    @GetMapping(value = "/getFeedback")
    public ResultView getFeedback(String feedbackId){
        if(feedbackId==null){
            return  ResultView.error(ResultEnum.CODE_125);
        }
        return wxFeedbackService.getFeedback(feedbackId);
    }

    /***
     * 反馈处理
     *  @param feedbackId 意见编号
     * @return
     */
    @PostMapping(value = "/upFeedback")
    public ResultView upFeedback(String feedbackId){
        if(feedbackId==null){
            return  ResultView.error(ResultEnum.CODE_125);
        }
        return wxFeedbackService.upFeedback(feedbackId)>0?ResultView.ok():ResultView.error(ResultEnum.CODE_2);
    }
}
