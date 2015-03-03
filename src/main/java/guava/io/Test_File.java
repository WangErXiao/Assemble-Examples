package guava.io;


import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by root on 15-3-3.
 */
public class Test_File {
    public static void main(String[]args) throws IOException {
        File file=new File(Test_File.class.getClass().getResource("/test.txt").getFile());
        List<String> lines=null;
        lines= Files.readLines(file, Charsets.UTF_8);
        for (String  xx:lines){
            System.out.println(xx);
        }
    }
}
