package concurrent.atomic;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by robin on 5/2/15.
 */
public class TestAtomicIntgerFieldUpdater {

    @Test
    public void updaterField() throws InterruptedException {
        AtomicIntegerFieldUpdater<People> ageFieldUpdater=AtomicIntegerFieldUpdater.newUpdater(People.class,"age");
        People people=new People("robin",22,1);

        ExecutorService service= Executors.newCachedThreadPool();
        service.submit(new Task(ageFieldUpdater,people));
        service.submit(new Task(ageFieldUpdater,people));
        service.submit(new Task(ageFieldUpdater,people));
        service.submit(new Task(ageFieldUpdater,people));
        service.shutdown();
        service.awaitTermination(100, TimeUnit.SECONDS);
        Assert.assertTrue(people.getAge()==62);
    }
    static class Task implements Runnable{

        private AtomicIntegerFieldUpdater<People> atomicIntegerFieldUpdater;
        private People people;

        public Task(AtomicIntegerFieldUpdater<People> atomicIntegerFieldUpdater, People people) {
            this.atomicIntegerFieldUpdater = atomicIntegerFieldUpdater;
            this.people = people;
        }

        @Override
        public void run() {
           for (int i=0;i<10;i++){
              atomicIntegerFieldUpdater.incrementAndGet(people);
           }
        }
    }



}
class People{
        private String name;
        volatile int age;  //必须是 int 和 volatile
        private int sex;

        public People(String name, Integer age, int sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }

