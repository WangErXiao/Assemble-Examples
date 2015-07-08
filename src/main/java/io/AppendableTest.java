package io;

import java.io.IOException;

/**
 * Created by yao on 15/7/7.
 */
public class AppendableTest {
    public static void main(String[]args) throws IOException {

        Appendable appendable=new StringBuffer();
        appendable.append("abc");
        appendable.append("abcdefg");
        System.out.println(appendable.toString());

        System.out.println(~1);

    }
}
