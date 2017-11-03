package com.zhongan.icare.message.push.util;

import java.io.Serializable;

/**
 * Created by zhangxiaojun on 2017/5/4.
 */
public class CardBean implements Serializable
{
    private static final long serialVersionUID = 1481255861525L;

    private String eno;

    private String epassword;

    private String edenomination;

    public String getEno()
    {
        return eno;
    }

    public void setEno(String eno)
    {
        this.eno = eno;
    }

    public String getEpassword()
    {
        return epassword;
    }

    public void setEpassword(String epassword)
    {
        this.epassword = epassword;
    }

    public String getEdenomination()
    {
        return edenomination;
    }

    public void setEdenomination(String edenomination)
    {
        this.edenomination = edenomination;
    }
}
