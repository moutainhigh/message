package com.zhongan.icare.message.push.constants;

/**
 * 存放data的key
 * Created by zhangxiaojun on 2016/12/12.
 */
public interface ConstantsDataKey
{
    /**
     * redis前缀
     */
    public static String REDIS_GROUP_CODE_PRE = "icare-message.group-code.";
    /**
     * redis前缀
     */
    public static String REDIS_GROUP_ALL = "icare-message.group-all";
    /**
     * redis前缀
     */
    public static String REDIS_TEMPLATE_CODE_PRE = "icare-message.template-code.";
    /**
     * redis前缀
     */
    public static String REDIS_TEMPLATE_All = "icare-message.template-all";

    /**
     * 自定义字段里面的：消息跳转的uri
     */
    public static String CUSTOMER_URI = "uri";

    /**
     * 自定义字段里面的：分组
     */
    public static String CUSTOMER_GROUPCODE = "groupCode";


    /**
     * 消息的标题
     */
    public static String DATA_KEY_TITLE = "title";

    /**
     * 消息的描述
     */
    public static String DATA_KEY_DESCRIPTION = "description";

    /**
     * 消息的正文
     */
    public static String DATA_KEY_CONTENT = "content";


    /**
     * 请求消息的id
     */
    public static String REQUEST_LOG_ID = "request_log_id";


    /**
     * 消息类型
     */
    public static String REQUEST_MESSAGE_TYPE = "request_message_type";

    /**
     * 第三方 模板
     */
    public static String THIRD_TEMPLATE_CODE = "third_template_code";

    /**
     * 通知标题，可以为空；如果为空则设为appid对应的应用名;
     */
    public static String ANDROID_TITLE = "title";

    /**
     * 通知文本内容，不能为空;
     */
    public static String ANDROID_DESCRIPTION = "description";

    /**
     * android客户端自定义通知样式，如果没有设置默认为0;
     */
    public static String ANDROID_NOTIFICATION_BUILDER_ID = "notification_builder_id";

    /**
     * android客户端,只有notification_builder_id为0时有效，可以设置通知的基本样式包括(响铃：0x04;振动：0x02;可清除：0x01;),这是一个flag整形，每一位代表一种样式,如果想选择任意两种或三种通知样式，notification_basic_style的值即为对应样式数值相加后的值。
     */
    public static String ANDROID_NOTIFICATION_BASIC_STYLE = "notification_basic_style";

    /**
     * android客户端，点击通知后的行为(1：打开Url; 2：自定义行为；); open_type = 1，url != null：打开网页； open_type = 2，pkg_content = null：直接打开应用； open_type = 2，pkg_content != null：自定义动作打开应用。
     */
    public static String ANDROID_OPEN_TYPE = "open_type";

    /**
     * url：需要打开的Url地址，open_type为1时才有效;
     */
    public static String ANDROID_URL = "url";

    /**
     * open_type为2时才有效，Android端SDK会把pkg_content字符串转换成Android Intent,通过该Intent打开对应app组件，所以pkg_content字符串格式必须遵循Intent uri格式，最简单的方法可以通过Intent方法toURI()获取
     */
    public static String ANDROID_PKG_CONTENT = "pkg_content";

    /**
     * 自定义内容，键值对，Json对象形式(可选)；在android客户端，这些键值对将以Intent中的extra进行传递。
     */
    public static String ANDROID_CUSTOM_CONTENT = "custom_content";

    /**
     * 其内容可以为字符串或者字典，如果是字符串，那么将会在通知中显示这条内容;
     */
    public static String IOS_ALERT = "alert";

    /**
     * 指定通知展现时伴随的提醒音文件名。如果找不到指定的文件或者值为 default，那么默认的系统音将会被使用。如果为空，那么将没有声音;
     */
    public static String IOS_SOUND = "sound";

    public static String IOS_CONTENT_AVAILABLE = "content-available";

    /**
     * 其值为数字，表示当通知到达设备时，应用的角标变为多少。如果没有使用这个字段，那么应用的角标将不会改变。设置为 0 时，会清除应用的角标;
     */
    public static String IOS_BADGE = "badge";

    /**
     * IOS的aps字段
     */
    public static String IOS_APS = "aps";


    public static String IOS_SCHEME = "scheme";

    /**
     * ios_android_data_map
     */
    public static String DATA_MAP_IOS_ANDROID = "data_map_ios_android";


    /////////////消息分组类型-start//////////////
    /**
     * 积分变动
     */
    public static String GROUP_CODE_1 = "GROUP_CODE_1";
    /**
     * 体检
     */
    public static String GROUP_CODE_2 = "GROUP_CODE_2";

    /**
     * 保险
     */
    public static String GROUP_CODE_3 = "GROUP_CODE_3";

    /**
     * 购物
     */
    public static String GROUP_CODE_4 = "GROUP_CODE_4";

    /**
     * 外卖订餐
     */
    public static String GROUP_CODE_5 = "GROUP_CODE_5";

    /**
     * 京东e卡
     */
    public static String GROUP_CODE_6 = "GROUP_CODE_6";

    /////////////消息分组类型-end//////////

}
