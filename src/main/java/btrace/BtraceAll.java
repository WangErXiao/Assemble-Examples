package btrace;

import com.sun.btrace.annotations.*;

/**
 * Created by root on 15-3-24.
 */
@BTrace
public class BtraceAll {
    @TLS
    private static long beginTime;

    @OnMethod(clazz = "java.lang.ClassLoader",
                method = "defineClass")
    public static void traceMethodBegin(){
        beginTime=System.currentTimeMillis();
    }

    @OnMethod(
            clazz="java.lang.ClassLoader",
            method="defineClass",
            location=@Location(Kind.RETURN)
    )
    public static void traceMethdReturn(
            @Return String result,
            @ProbeClassName String clazzName,
            @ProbeMethodName String methodName){
        System.out.println("===========================================================================");
        System.out.println(clazzName+"."+ methodName);
        System.out.println("Time taken : "+(System.currentTimeMillis() - beginTime));
        System.out.println("java thread method trace:---------------------------------------------------");
        StackTraceElement []elements= Thread.currentThread().getStackTrace();
        for (StackTraceElement element:elements){
            System.out.println(element);
        }
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Reuslt :"+result);
        System.out.println("============================================================================");
    }


}
