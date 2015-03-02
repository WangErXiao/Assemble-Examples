package socke.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 15-3-2.
 */
public class LearnHttp {

    private static final byte CR = '\r';

    private static final byte LF = '\n';

    private static final byte[]CRLF={CR,LF};


    public void testHttp() throws IOException {
        String host="www.baidu.com";
        Socket socket=new Socket(host,80);

        OutputStream outputStream=socket.getOutputStream();
        InputStream inputStream=socket.getInputStream();
        writeRequest(outputStream,host);
        readResponse(inputStream);
        System.out.println("\n");
    }


    private void writeRequest(OutputStream out,String host) throws IOException {
        out.write("GET /index.html HTTP/1.1".getBytes());
        out.write(CRLF);

        out.write(("Host: "+host).getBytes());
        out.write(CRLF);
        out.write(CRLF);

        out.flush();
    }
    private void readResponse(InputStream in) throws IOException {
        String statusLine=readStatusLine(in);
        System.out.println("statusLine :"+statusLine);
        Map headers=readHeaders(in);
        int contentLength=Integer.valueOf(headers.get("Content-Length").toString());
        byte[]body=readResponseBody(in,contentLength);
        String charset=headers.get("Content-Type").toString();
        if(charset.matches(".+;charset=.+")) {
            charset = charset.split(";")[1].split("=")[1];
        } else {
            charset = "ISO-8859-1";     // 默认编码
        }
        System.out.println("content:\n" + new String(body, charset));
    }

    private byte[]readResponseBody(InputStream in,int contentLength) throws IOException {
        ByteArrayOutputStream buff=new ByteArrayOutputStream(contentLength);
        int b;
        int count=0;
        while (count++<contentLength){
            b=in.read();
            buff.write(b);
        }
        return  buff.toByteArray();
    }

    private Map readHeaders(InputStream in) throws IOException {
        Map headers=new HashMap();
        String line;
        while (!("".equals(line=readLine(in)))){
            String[]kv=line.split(": ");
            headers.put(kv[0],kv[1]);
        }
        return headers;
    }

    private String readStatusLine(InputStream in) throws IOException {

        return readLine(in);
    }

    private String readLine(InputStream in) throws IOException {
        int b;
        ByteArrayOutputStream buff=new ByteArrayOutputStream();
        while ((b=in.read())!=CR){
            buff.write(b);
        }
        in.read();
        String line=buff.toString();
        return line;
    }

    public static void main(String[]args) throws IOException {
        for (int i=0;i<1;i++){

            new Thread(){
                @Override
                public void run() {
                    try {
                        new LearnHttp().testHttp();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
