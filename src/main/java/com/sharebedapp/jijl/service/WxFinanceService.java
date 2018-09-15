package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxFinance;
import com.sharebedapp.jijl.model.wrap.RevenueInfo;
import com.sharebedapp.jijl.model.wrap.UserFinance;
import com.sharebedapp.jijl.result.ResultView;
import com.github.pagehelper.PageInfo;

public interface WxFinanceService {
    ResultView payForBail(String userId, String price);

    void payCallback(String outTradeNo, Integer payType);

    ResultView backBail(String userId);

    WxFinance isBail(String userId);

    /** ��ȡ�����б�
     * @param pageNo ��ҳҳ��
     * @param pageSize ��ҳ��С
     * @param hospitalName ҽԺ����
     * @param categoryId �豸����id
     * @param financeType ��������
     * @return ƽ̨���в����б�
     */
    PageInfo getFinanceList(Integer pageNo, Integer pageSize, String hospitalName, String categoryId, Integer financeType);

    WxFinance getByAgentId(String agentId);

    /**
     * ��ѯ�û�������Ϣ
     * @param userId ������id
     * @param agentId ������id
     * @param days ��ѯ������ǰ�Ĳ����¼
     * @param startTime ��ѯ��ʼʱ��
     * @param endTime ��ѯ����ʱ��
     * @param hospitalId Ҫ��ѯ��ҽԺ�б�
     * @return �����û��Ĳ�����Ϣ
     */
    UserFinance getFinanceByUser(String userId, String agentId, Integer days, String startTime, String endTime, String hospitalId);

    /**
     * ��ȡѺ���б�
     * @param userPhone �û��ֻ���
     * @param nickName �û��ǳ�
     * @param pageNo ��ҳҳ��
     * @param pageSize ��ҳ��С
     * @return Ѻ���б�
     */
    ResultView getFinanceBailList(String userPhone, String nickName, Integer pageNo, Integer pageSize);

    /**
     * ��ȡƽ̨������Ϣ
     * @return ������Ϣ
     */
    RevenueInfo getRevenueInfo();
}
