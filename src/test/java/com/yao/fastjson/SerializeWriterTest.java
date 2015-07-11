package com.yao.fastjson;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by yao on 15/7/11.
 */
public class SerializeWriterTest extends TestCase {

    @Test
    public void testAppend() throws Exception {
        SerializeWriter serializeWriter=new SerializeWriter();
        serializeWriter.append("yaozhibin");
        System.out.println(serializeWriter.toString());
        serializeWriter.close();
        serializeWriter=new SerializeWriter();
        System.out.println(serializeWriter.toString());

    }

    public void testToString() throws Exception {

    }
}