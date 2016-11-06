package gc;

import java.util.UUID;

/**
 * Created by robin on 11/6/16.
 *
 * String intern 导致ygc越来越慢
 * 代码文章参考来源：
 * http://mp.weixin.qq.com/s?__biz=MzIzNjI1ODc2OA==&mid=2650886867&idx=1&sn=e4433f7068357b0f9ed283b607fa50e6&chksm=f32f666cc458ef7a0132c6dfb74bc53626b47d884db7ae1b29a41bea3527e416c87c71c49fbc&mpshare=1&scene=1&srcid=11064UKVJjjcb3kCW22TgGEh#rd
 *
 * JVM 参数： -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -Xmx256M -Xms256M -Xmn100M
 *  String.intern()作用是是同值字符串在jvm 内存中只保留一份值，不同的string对象 值都指向同一内存值
 *  现象：String.intern()ygc 越来越慢
 *  GC Allocation Failure 表示触发ＧＣ的原因是没有足够的空间分配新的数据
 *
 *  每次通过 jmap -histo:live <pid>通过命令触发fullgc后，ygc的消费时间又变短
 *
 *  原因：ＵＵＩＤ String.intern()  方法会往ＪＶＭ Heap 中的StringTable 不断插入新值，使ＳｔｒｉｎｇＴａｂｌｅ越来越大，
 *  ygc过程需要对StringTable做扫描，以保证处于新生代的String代码不会被回收掉
 *  在Full GC或者CMS GC过程会对StringTable做清理，具体验证很简单，执行下jmap -histo:live <pid>，将会发现YGC的时候又降下去了
 */
public class YgcForStringIntern {
    public static void main(String args[]){
        for(int i=0;i<100000000;i++){
            uuid();
        }

    }

    public static void uuid(){
        UUID.randomUUID().toString().intern();
    }
}
