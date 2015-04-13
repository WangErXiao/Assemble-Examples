package javabasic.autoclose;

/**
 * Created by yaozb on 15-4-13.
 * 可以在try(){
 *
 * }catch-finally语法块里，自动释放资源
 */
public class TestAutoClose implements AutoCloseable {
    public void method(){
        System.out.println("invoke method............");
    }
    @Override
    public void close() throws Exception {
        System.out.println("close resource.............");
    }
    public static void main(String[]args){
        try(TestAutoClose testAutoClose=new TestAutoClose()){
            testAutoClose.method();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
