package printLineOfSource;

/**
 * Created by root on 15-3-24.
 */
public class LineOfSource {
    public static void main(String[]args){
        StackTraceElement[]stackTraceElements=Thread.currentThread().getStackTrace();
        System.out.println("["+getFileName()+"："+ getLineNumber()+"]"+"Hello World!");
    }
    public static int getLineNumber(){
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static String getFileName(){
        return Thread.currentThread().getStackTrace()[2].getFileName();
    }
}
