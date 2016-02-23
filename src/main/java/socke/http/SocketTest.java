package socke.http;

import java.io.*;
import java.net.Socket;

/**
 * Created by yao on 16/1/27.
 */
public class SocketTest {

    public static void main(String[]args) throws IOException {

        Socket socket=new Socket("127.0.0.1",9000);


        InputStream inputStream=socket.getInputStream();

        OutputStream outputStream=socket.getOutputStream();

        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream));

        writer.write("nihao\n");
        writer.write("nihao1\n");
        writer.write("nihao2\n");
        writer.flush();
        String line;
        while ((line=reader.readLine())!=null){
            System.out.println(line);
        }
        writer.close();
        reader.close();
    }
}
