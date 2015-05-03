package algorithm;

import com.sun.btrace.BTraceUtils;

import java.util.BitSet;
import java.util.Random;

/**
 * Created by robin on 5/3/15.
 */
public class BloomFilter {
    //BitSet 初始分配位数
    private static final int DAFAULT_SIZE=1<<25;
    private static final int []SEEDS={5,7,11,13,31,37,61};
    private BitSet bits=new BitSet(DAFAULT_SIZE);
    private SimpleHash[]funcs=new SimpleHash[SEEDS.length];
    public BloomFilter(){
        for (int i=0;i<SEEDS.length;i++){
           funcs[i]=new SimpleHash(DAFAULT_SIZE,SEEDS[i]);
        }
    }
    public BitSet getBits(){
        return bits;
    }
    public void add(String value){
        for (SimpleHash f:funcs){
           bits.set(f.hash(value),true);
        }
    }

    public boolean contains(String value){
        if(value==null){
            return false;
        }
        boolean result=true;
        for (SimpleHash f:funcs){
            result=result&&bits.get(f.hash(value));
        }
        return result;
    }


    public static class SimpleHash{
        private int cap;
        private int seed;
        public SimpleHash(int cap,int seed){
            this.cap=cap;
            this.seed=seed;
        }
        public int hash(String value){
            int result=0;
            int len=value.length();
            for(int i=0;i<len;i++){
                result=seed*result+value.charAt(i);
            }
            return (cap-1)&result;
        }

    }

    //test

    public static void main(String[]args){
        BloomFilter bloomFilter=new BloomFilter();
        Random random=new Random();
        for(int i=-10;i<10;i++){
            bloomFilter.add(i + "");
        }
        long start= System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            bloomFilter.contains(0 + "");
        }
        System.out.println("contain function consume time:"+((System.currentTimeMillis()-start)+0.0)/(10000*1000)+"secs");
    }
}
