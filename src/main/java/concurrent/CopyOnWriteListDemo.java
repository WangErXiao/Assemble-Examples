package concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by root on 15-3-22.
 */
public class CopyOnWriteListDemo {

    private static class WritToList implements Runnable{
        private List<String>list;
        private int index;
        private WritToList(List<String> list,int index) {
            this.list = list;
            this.index=index;
        }
        @Override
        public void run() {
            list.remove(index);
            list.add(index,"write_"+index);
        }

    }
    private static class ReadList implements Runnable{
        private List<String>list;

        private ReadList(List<String> list) {
            this.list = list;
        }
        @Override
        public void run() {
            for (Object o:list){
                System.out.println(o);
            }
        }
    }
    public static void main(String[]args){
        ExecutorService executorService= Executors.newCachedThreadPool();
        //List<String> list1 = new ArrayList<String>();
        List<String> list = new CopyOnWriteArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add("main_" + i);
        }
        for (int i = 0; i < 100; i++) {
            executorService.execute(new ReadList(list));
            executorService.execute(new WritToList(list, i));
        }
        executorService.shutdown();
    }
}
