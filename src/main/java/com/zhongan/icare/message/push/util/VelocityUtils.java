package com.zhongan.icare.message.push.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.*;

/**
 * Created by zhangxiaojun on 2016/12/12.
 */
public class VelocityUtils
{

    /**
     * 替换模板中的变量
     *
     * @param contextMap 需要替换的变量
     * @param content    模板内容
     * @return
     */
    public static String convertData(VelocityEngine velocityEngine, Map<String, Object> contextMap, String content)
    {
        // 取得velocity的上下文context
        VelocityContext context = new VelocityContext();
        Iterator<Map.Entry<String, Object>> iterator = contextMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            // 把数据填入上下文
            Map.Entry<String, Object> entryNext = iterator.next();
            context.put(entryNext.getKey(), entryNext.getValue());
        }
        // 输出流
        StringWriter writer = new StringWriter();
        // 转换输出
        velocityEngine.evaluate(context, writer, "", content); // 关键方法
        return writer.toString();
    }


    public static void main(String[] args)
    {
        Map<String, Object> contextMap = new HashMap();
//        contextMap.put("title", "#袁平安邀你运动# 快来报名参加，我发起的代码revie活动！不负青春，嗨起来！！");
//        contextMap.put("link", "https://test.zuifuli.io/api/gateway/app/lanuch?appCode=club&channelCode=sz&redirect=http://wechat.zhongan.com/Dev_8XUA0HY/suite/xieyingshuang/club/activity/detail.html?activityId=432&corpid=ifuli1058001&_source=ifuli");
//        contextMap.put("picLink", "https://dev-cdn.zhongan.com/a00000/Project/WecorpSuite/Club/V3/img/share_img.png");
//        String content = "{\"content\":\"#if($content)<div style='margin:20px 0px 0px 0px;font-size:15px;color:#333333'>$!content</div>#else $!content  #end\",\"title\":\"$title\",\"image\":\"$!picLink\",\"details\":\"查看详情\",\"detailsUrl\":\"$!link\"}";
//        String content = "#foreach( $card in $cardList) <div style='font-size: 14px;font-family:Helvetica;margin: 15px 0px 15px 0px;color:#a1a1a1;'><p style='margin: 5px 0px 3px 0px;'>卡号:&nbsp;$!card.eNo</p ><p style='margin: 5px 0px 3px 0px;'>密码:&nbsp;$!card.ePassword</p ><p style='margin: 5px 0px 3px 0px;'>面额:&nbsp;$!card.eDenomination元</p ></div><hr> #end";
        String content = "{\"content\":\"#foreach( $card in $cardList) <div style='font-size:14px;color:#999999;padding:12px 0px 0px;'><p style='margin: 3px 0px;'>面值:&nbsp;$!card.edenomination元</p ><p style='margin: 3px 0px;'>卡号:&nbsp;$!card.eno</p ><p style='margin: 3px 0px;'>密码:&nbsp;$!card.epassword</p ><div style='width:100%;height:1px;background:#dddddd;margin:12px 0px 0px 0px;'></div></div> #end\",\"description\":\"$!description\",\"title\":\"$title\",\"details\":\"$!details\",\"detailsUrl\":\"$!detailsUrl\"}";
        List<CardBean> cardList = new ArrayList<>();
        CardBean cardBean1 = new CardBean();
        cardBean1.setEno("TTGY_000001");
        cardBean1.setEdenomination("5000");
        cardBean1.setEpassword("kbrn12");
        cardList.add(cardBean1);

//        CardBean cardBean2 = new CardBean();
//        cardBean2.seteNo("2222222222");
//        cardBean2.seteDenomination("京东卡2");
//        cardBean2.setePassword("bbbbb");
//        cardList.add(cardBean2);
//        CardBean cardBean3 = new CardBean();
//        cardBean3.seteNo("333333333");
//        cardBean3.seteDenomination("京东卡3");
//        cardBean3.setePassword("cccccccc");
//        cardList.add(cardBean3);

        contextMap.put("cardList", cardList);
//        contextMap.put("detailsUrl", "");
        contextMap.put("description", "购买成功，会下发卡密到你的手机\\n一经购买，不能退货");
        contextMap.put("details", "查看详情");
        contextMap.put("title", "天天果园");
        VelocityEngine v = new VelocityEngine();
        v.init();
        String s = convertData(v, contextMap, content);
        System.out.println(s);
        JSONObject newJsonObject = JSONObject.parseObject(s);

    }

}
