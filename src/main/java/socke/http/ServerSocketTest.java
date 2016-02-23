package socke.http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yao on 16/1/27.
 */
public class ServerSocketTest {

    public static void main(String[]args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(9000);
        while (true){
            Socket socket= serverSocket.accept();
            InputStream inputStream= socket.getInputStream();
            OutputStream outputStream=socket.getOutputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream));
            String line;
            while ((line=reader.readLine())!=null){
                System.out.println("received:"+line);
                writer.write("i received your msg!");
                writer.flush();
            }
            reader.close();
            writer.close();
        }
    }
}
