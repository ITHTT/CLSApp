package com.tysci.cls.networks;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/29.
 */
public class HttpUrls {
    public static final String HOST_URL="http://csl.ballq.cn/";

    public static final String HTTP_HOST_URL="http://csl.ballq.cn/api/v1/";

    //public static final String HOST_URL="http://192.168.10.21:8080/";

    //public static final String HTTP_HOST_URL="http://192.168.10.21:8080/api/v1/";

    /**获取微信Token*/
    public static final String GET_WECHAT_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token";
    /**获取微信用户信息*/
    public static final String GET_WECHAT_USER_IFNO_URL="https://api.weixin.qq.com/sns/userinfo";

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL)
    {
        String strAllParam=null;
        String[] arrSplit=null;

        strURL=strURL.trim().toLowerCase();

        arrSplit=strURL.split("[?]");
        if(strURL.length()>1)
        {
            if(arrSplit.length>1)
            {
                if(arrSplit[1]!=null)
                {
                    strAllParam=arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    public static Map<String, String> URLRequest(String URL)
    {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit=null;

        String strUrlParam=TruncateUrlPage(URL);
        if(strUrlParam==null)
        {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit)
        {
            String[] arrSplitEqual=null;
            arrSplitEqual= strSplit.split("[=]");

            //解析出键值
            if(arrSplitEqual.length>1)
            {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            }
            else
            {
                if(arrSplitEqual[0]!="")
                {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }


}
