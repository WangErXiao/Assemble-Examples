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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;

/**
 * Created by yao on 15/6/19.
 *
 * post 请求 加cookie
 */
public class PostUrl {
    private static String cookieStr=null;

    private static Map<String,Integer>IP_PORTS=new HashMap<>();
    private static List<String>IP_LIST=new ArrayList<>();
    static {
        File file=new File("/Users/yao/tmp/ip4.txt");
        BufferedReader reader= null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line=null;
        try {
            while ((line=reader.readLine())!=null){
                String[]ipPort=line.split(":");
                IP_PORTS.put(ipPort[0],Integer.parseInt(ipPort[1]));
                IP_LIST.add(ipPort[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[]args) throws InterruptedException, IOException {


        //对应表
        Map<String,String> imgs=new HashMap<>();
        imgs.put("海龟","dienskiden");
        imgs.put("大熊猫","dopkdmemj");
        imgs.put("大象","kdjieoemd");
        imgs.put("骆驼","djildmc");
        imgs.put("金丝猴", "peicjed");
        imgs.put("藏羚羊","xjeifhjske");
        //"xjeifhjske",
        //"dienskiden",
        //"djildmc",
        //"kdjieoemd"
        //"dopkdmemj",
        //"peicjed",

        for(int i=50;i<256000;i++){

            try {
                String ip = "1.1.1.";
                String code = "http://www.noahedu.com/noahHoliday/admin/holiday/getCode?v=" + System.currentTimeMillis();
                String holidayid="";
                if(i%2==0){
                    holidayid="69";
                }else{
                    holidayid="69";
                }
                String url = "http://www.noahedu.com/noahHoliday/admin/user/addpraise?holidayid="+holidayid+"&code=";

                JSONObject jsonObject = JSONObject.parseObject(postUrl(code, null, null, i));

                //设置cookie
                CookieStore cookieStore = new BasicCookieStore();
                BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", cookieStr);
                cookie.setDomain("www.noahedu.com");
                cookie.setPath("/noahHoliday/");
                cookieStore.addCookie(cookie);

          /*  BasicClientCookie lpvt = new BasicClientCookie("Hm_lpvt_25aaa45e41616a1ea24f875b24b38c3b","1435999601");
            lpvt.setDomain(".noahedu.com");
            lpvt.setPath("/");
            cookieStore.addCookie(lpvt);

            BasicClientCookie lvt = new BasicClientCookie("Hm_lvt_25aaa45e41616a1ea24f875b24b38c3b","1425995890,1425996252,1425996715,1425997571");
            lvt.setDomain(".noahedu.com");
            lvt.setPath("/");
            cookieStore.addCookie(lvt);*/


                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, 3);
                Date date = calendar.getTime();
                cookie.setExpiryDate(date);

                if (jsonObject != null && jsonObject.get("msgCode") != null && jsonObject.get("msgCode").toString().equals("309")) {
                    String pic = ((Map) jsonObject.get("data")).get("pic_name").toString();
                    String codeStr = imgs.get(pic);

                    List<String> mds =(List<String>)((Map) jsonObject.get("data")).get("mds");
                    List<String> pics =(List<String>)((Map) jsonObject.get("data")).get("pics");
                    url += mds.get(pics.indexOf(codeStr));
                    System.out.println(postUrl(url, null, cookieStore, i));
                }
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }


    }
    public static String postUrl(String url, List<NameValuePair> nvps,CookieStore cookie,int i) {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
        if (cookie!=null)
            httpclient.setCookieStore(cookie);
        try {
            HttpPost httpost = new HttpPost(url);
            String ip=IP_LIST.get(i%IP_LIST.size());
            Integer port=IP_PORTS.get(ip);
            HttpHost proxy = new HttpHost(ip, port, "http");
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);



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
