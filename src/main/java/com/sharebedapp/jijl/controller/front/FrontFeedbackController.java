package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.model.WxFeedback;
import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxFeedbackService;
import com.sharebedapp.jijl.service.WxUserService;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/front")
public class FrontFeedbackController {
    @Autowired
    private WxFeedbackService wxFeedbackService;
    @Autowired
    private WxUserService wxUserService;
    @Value("${web.portrait-path}")
    private String portraitPath;

    /**
     * 意见反馈
     *
     * @param feedbackType 意见类型【1.设备问题 2.支付问题 3.其他问题】
     * @param context      意见内容
     * @param file         意见图片
     * @return
     */
    @PostMapping(value = "/addFeedback")
    public ResultView addFeedback(HttpServletRequest request, Integer feedbackType, String context,String file) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        WxUser user = wxUserService.getByUserId(userId);
        if (feedbackType == null) {
            return ResultView.error(ResultEnum.CODE_155);
        }
        if (feedbackType != 1 && feedbackType != 2 && feedbackType != 3) {
            return ResultView.error(ResultEnum.CODE_156);
        }
        if (StringUtil.isEmpty(context)) {
            return ResultView.error(ResultEnum.CODE_124);
        }
        if(StringUtil.isEmpty(file)){
            return ResultView.error(ResultEnum.CODE_153);
        }
        WxFeedback wxFeedback = new WxFeedback();
        wxFeedback.setFeedbackId(IdentityUtil.identityId("FB"));
        wxFeedback.setUserId(userId);
        wxFeedback.setFeedbackType(feedbackType);
        wxFeedback.setContext(context);
        wxFeedback.setFeedbackUrl(file);
        wxFeedback.setUserPhone(user.getUserPhone());
        wxFeedback.setStatus(1);
        wxFeedback.setcTime(new Date());
        return wxFeedbackService.insertFeedback(wxFeedback) > 0 ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);

    }


}
