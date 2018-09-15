package com.sharebedapp.jijl.result;
/**
 * @Author: jijl
 * @Description: 常量
 * @Date: 2018/7/2 16:54
 **/
public class ResultStatus {

    public static final String WMWLSHAREBED = "wmwlShareBed_";

    public static final String USER_ID = "userId";

    public static final String AGENT_ID = "agentId";

    public static final String MANAGER_ID = "managerId";

    public static final String USER = "user";

    public static final String MANAGER = "manager";

    public static final String WXAGENT = "agent";

    public static final String TOKEN = "authorization";

    public static final String WX_APP = "APP";//微信App支付

    public static final String WX_NATIVE = "NATIVE";//微信网页支付

    public static final String WX_JSAPI = "JSAPI";//微信公众号

    public static final String ALI_APP = "AL_APP";//支付宝APP

    public static final String ALI_WEB = "ALI_WEB";//支付宝网页

    public static final String WX_SP = "WX_SP";//微信小程序

    public static final String SESSION_KEY="session_key";

    public static final int EXISTED = -1;

    // 1：微信  2：QQ  3：微博  4:普通登陆
    public static final int WX_OPENID = 1;

    public static final int QQ_OPENID = 2;

    public static final int WB_OPENID = 3;

    public static final int PHONE_LOGIN = 4;

    //用户token的有效期---7天
    public static final int TONKEN_OUT_TIME = 604800;

    public static final int ADMIN = 1;

    public static final int TEMPORARY_ADMIN = 2;

    public static final int AGENT = 3;

    public static final int INTERVAL_REPEAT = -3;

    //订单状态:1:待支付 2:进行中 3:已完成
    public static final int ORDERSTATUS_PAY = 1;
    public static final int ORDERSTATUS_IN = 2;
    public static final int ORDERSTATUS_Y = 3;

    //1:无效 2:有效---订单表、财务表公用
    public static final int ISFLAG_N = 1;
    public static final int ISFLAG_Y = 2;

    public static final int AGENT_EXISTED = -1;

    public static final int PROPORTION_OVER = -2;

    public static final int PHONE_EXISTED = -3;

    //参数类型:1:押金设置 2:客服电话 3.每天可提现次数 4.最低提现金额 5:提现须知
    public static final int SYSTEMTYPE_1 = 1;
    public static final int SYSTEMTYPE_2 = 2;
    public static final int SYSTEMTYPE_WITHDRAW_AMOUNT = 3;
    public static final int SYSTEMTYPE_WITHDRAW = 4;
    public static final int SYSTEMTYPE_5 = 5;
    public static final int SYSTEMTYPE_6 = 6;
    public static final int SYSTEMTYPE_7 = 7;
    public static final int SYSTEMTYPE_8 = 8;

    public static final int NOT_EXISTED = -4;

    //商户类型
    public static final int MANAGERTYPE_3=3;

    public static final String EQUIPMENTID_TOP = "EQ";
    public static final String BATTERY_TOP = "BA";
    public static final String BUSINESS_TOP = "BUS";
    public static final String MANAGER_TOP = "MA";
    public static final String FEEDBACK_TOP = "FE";
    public static final String RECHARGE_TOP = "RE";
    public static final String RECHARGE_OUTTRADENO_TOP = "ROG";
    public static final String ORDER_TOP = "ORD";
    public static final String OUTTRADENO_TOP = "OUT";
    public static final String LOSE_TOP = "OLE";
    public static final String FINANCE_TOP = "FIN";
    public static final String MESSAGE_TOP = "MES";
    public static final String CASHREQUEST_TOP = "CAS";
    public static final String USER_TOP = "US";
    public static final String COUPON_TOP = "COU";

    /**
     * 通过
     */
    public static final String TRUE = "true";
    /**
     * 驳回
     */
    public static final String FALSE = "false";

    //支付类型(1-余额  2-微信)
    public static final int PAYTYPE_BALANCE = 1;
    public static final int PAYTYPE_WX= 2;

    public static final int BINDED = -1;

    //'订单:1:无效 2:有效
    public static final int ORDER_ISFLAG_NO = 1;

    public static final int ORDER_ISFLAG_YES = 2;

    /**
     * 1:充值 2:共享订单 3.购买订单 4.设备收益 5:押金缴纳 6:押金退款 7:提现
     */
    public static final int FINANCE_TYPE_RECHARGE = 1;
    public static final int FINANCE_TYPE_SHARE = 2;
    public static final int FINANCE_TYPE_BUY = 3;
    public static final int FINANCE_TYPE_EQUIP = 4;
    public static final int FINANCE_TYPE_DEPOSIT = 5;
    public static final int FINANCE_TYPE_REBACK = 6;
    public static final int FINANCE_TYPE_WITHDRAM = 7;

    //财务支付类型：1.余额 2.微信 3.支付宝 4.优惠卷
    public static final int FINANCE_PAYTYPE_1 = 1;
    public static final int FINANCE_PAYTYPE_2 = 2;
    public static final int FINANCE_PAYTYPE_3 = 3;
    public static final int FINANCE_PAYTYPE_4 = 4;

    //财务类型:1:余额 2:保证金
    public static final int FINANCE_BALANCETYPE_1 = 1;
    public static final int FINANCE_BALANCETYPE_2 = 2;

    //消息类型 1：我的消息 2：通知
    public static final int MESSAGE_1 = 1;
    public static final int MESSAGE_2 = 2;

    //1:空闲 2:使用中 3:故障 4:报失
    public static final int BATTERY_STATUS_1=1;
    public static final int BATTERY_STATUS_2=2;
    public static final int BATTERY_STATUS_3=3;
    public static final int BATTERY_STATUS_4=4;

    /**
     * 用户类型【1.普通用户 2.维修人员 3.保洁人员】
     */
    public static final int WXUSER_USERTYPE_1=1;
    public static final int WXUSER_USERTYPE_2=2;
    public static final int WXUSER_USERTYPE_3=3;

    /**
     * 用户是否是购买商【1.否 2.是】
     */
    public static final int WXUSER_ISPURCHASER_1=1;
    public static final int WXUSER_ISPURCHASER_2=2;

    /**
     * 用户状态 0.授权中 1:正常 2:禁用
     */
    public static final int WXUSER_ISFLAG_0=0;
    public static final int WXUSER_ISFLAG_1=1;
    public static final int WXUSER_ISFLAG_2=2;



    /**
     * 1:充值 2:共享订单 3.购买订单 4.设备收益 5:押金缴纳 6:押金退款 7:提现
     */
    public static final int WXFINACE_FINANCEETYPE_1=1;
    public static final int WXFINACE_FINANCEETYPE_2=2;
    public static final int WXFINACE_FINANCEETYPE_3=3;
    public static final int WXFINACE_FINANCEETYPE_4=4;
    public static final int WXFINACE_FINANCEETYPE_5=5;
    public static final int WXFINACE_FINANCEETYPE_6=6;
    public static final int WXFINACE_BFINANCEETYPE_7=7;


    /**
     * 财务类型:1:余额 2:保证金
     */
    public static final int WXFINACE_BALANCETYPE_1=1;
    public static final int WXFINACE_BALANCETYPE_2=2;

    /**
     * 收费规则:【1.小时 2.次数】
     */
    public static final int RECHARGE_TYPE_H=1;
    public static final int RECHARGE_TYPE_N=2;
    /**
     * 图片保存地址
     */

    public static final String FEEDBACK_IMG_PATH = "feedbackImg";

    public static final String CATEGORY_IMG_PATH = "categoryImg";

    public static final String HOSPITAL_IMG_PATH = "hospitalImg";

    public static final String AFTERSALE_IMG_PATH = "afterSaleImg";

    /**
     * 设备状态 1：空闲 2：使用中 3.故障
     */
    public static final int EQUIOMENT_STATUS_1 = 1;

    public static final int EQUIOMENT_STATUS_USING = 2;

    public static final int EQUIOMENT_STATUS_FAILURE = 3;

    public static final int IS_FLAG_INVALID = 1;

    public static final int IS_FLAG_VALID = 2;

    public static final int IS_FLAG_NORMAL = 1;

    public static final int IS_FLAG_FORBIDEN = 2;

    public static final int IS_READ_UNREAD = 1;

    public static final int IS_READ_READ = 2;

    public static final String IS_NULL_NO = "notNull";

    /**
     * 认购订单状态 1:待支付 2:已支付 3:已取消
     */
    public static final int ORDERS_STATUS_UNPAY = 1;

    public static final int ORDERS_STATUS_PAY = 2;

    public static final int ORDERS_STATUS_CANCELED = 3;

    /**
     * 设备绑定状态
     */
    public static final int UNBINDING = 1;

    public static final int ISBINDING = 2;

    public static final int MACHINE_CLOUD_STATUS_SUCCESS=0;

    public static final int MACHINE_CLOUD_STATUS_ERROR=1;

    public static final int MACHINE_CLOUD_STATUS_FIAL=2;



    /**
     * 操作类型 1.维修 2.清洁 3.租用
     */
    public static final int RECORD_TYPE_MAINTAIN = 1;

    public static final int RECORD_TYPE_CLEAN = 2;

    public static final int ORECORD_TYPE_RENT = 3;

    /**
     * 1.审核中 2.通过 3.驳回
     */
    public static final int CASHREQUEST_STATUS_CHECK = 1;

    public static final int CASHREQUEST_STATUS_Y = 2;

    public static final int CASHREQUEST_STATUS_N = 3;
    /**
     * 是否绑定【1:未绑定 2：已绑定】
     */
    public static final int EQUIPMENT_IS_W = 1;

    public static final int EQUIPMENT_IS_Y = 2;

    /**
     * 1:模块 2:菜单 3:按钮
     */
    public static final int MENUTYPE_MODULE=1;

    public static final int  MENUTYPE_MENU=2;

    public static final int MENUTYPE_BUTTON=3;

    /**
     * 管理员权限权限  1 超级管理员
     */
    public static final int  MANAGERTYPE_1=1;

    public static final String  AUTHORITY_ROLE_ID="ROL";
    //角色表-菜单表:1:有效 2:无效
    public static final int AUTHORITY_ROLE_YES = 1;
    public static final int  AUTHORITY_ROLE_NO = 2;


    public static final String  AUTHORITY_MENU_TOP="MEN";
    public static final String  AUTHORITY="AUT";

    /**
     * 提现类型1:余额提现 2.收益提现
     */
    public static final int CASH_REQUEST_TYPE_BALANCE = 1;

    public static final int CASH_REQUEST_TYPE_REVENUE = 2;

    /**
     * 提现状态:1.审核中 2.通过 3.驳回
     */
    public static final int CASH_REQUEST_STATUS_CHECKING = 1;

    public static final int CASH_REQUEST_STATUS_PASS = 2;

    public static final int CASH_REQUEST_STATUS_REJECT = 3;

    public static final String FAILED = "failed";

    /**
     * 1科室,2病房,3床号类型
     */
    public static final int DEPT_TYPE_DEPART = 1;

    public static final int DEPT_TYPE_WARD = 2;

    public static final int DEPT_TYPE_BEDNUMBER = 3;

    /**
     * 设备电量
     */
    public static final Double KWH_MAX = 12.00;
    public static final Double KWH_MIN = 11.50;

    public static final int WITHDRAW_COUNT = -2;

    public static final int WITHDRAW_AMOUNT = -1;
    /**
     * 用户类型
     */
    public static final int WX_CUSTOMER = 1;

    public static final int WX_BUYER = 2;

    public static final int WX_AGENT = 3;

    public static final String USER_TYPE = "userType";
}
