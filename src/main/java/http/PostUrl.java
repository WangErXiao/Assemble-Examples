package http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.*;

/**
 * Created by yao on 15/6/19.
 *
 * post 请求 加cookie
 */
public class PostUrl {
    private static String cookieStr=null;

    public static void main(String[]args) throws InterruptedException {


        //对应表
        Map<String,String> imgs=new HashMap<>();
        imgs.put("海龟","hg01");
        imgs.put("大熊猫","dxm02");
        imgs.put("大象","dx03");
        imgs.put("骆驼","lt04");
        imgs.put("金丝猴", "jsh05");
        imgs.put("藏羚羊","zly06");

        for(int i=0;i<100;i++){

            String code="http://www.noahedu.com/noahHoliday/admin/holiday/getCode?v="+System.currentTimeMillis();

            String url="http://www.noahedu.com/noahHoliday/admin/user/addpraise?holidayid=1350&code=";

            JSONObject jsonObject= JSONObject.parseObject(postUrl(code, null, null));

            //设置cookie
            CookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", cookieStr);
            cookie.setDomain("www.noahedu.com");
            cookie.setPath("/noahHoliday/");
            cookieStore.addCookie(cookie);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, 3);
            Date date = calendar.getTime();
            cookie.setExpiryDate(date);

            if(jsonObject.get("msgCode").toString().equals("309")){
                String pic=((Map)jsonObject.get("data")).get("pic_name").toString();
                String codeStr=imgs.get(pic);
                url+=codeStr;
                if(!postUrl(url, null,cookieStore).equals("true")){
                    Thread.sleep(1000*5);
                }
            }
        }


    }
    public static String postUrl(String url, List<NameValuePair> nvps,CookieStore cookie) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        if (cookie!=null)
            httpclient.setCookieStore(cookie);
        try {
            HttpPost httpost = new HttpPost(url);
            //设置表单提交编码为UTF
            if (nvps != null)
                httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpost);
            for (Header header:response.getAllHeaders()){
                if(header.toString().contains("JSESSIONID")){
                    String[]tmp= header.toString().split("JSESSIONID=");
                    tmp=tmp[1].split(";");
                    cookieStr=tmp[0];
                }
            }
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);

            EntityUtils.consume(entity);
            return content;
        } catch (Exception e) {
            return null;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

}
