package ehcache;

/**
 * Created by root on 15-4-3.
 */
public class TestEhcahe {
    public static void main(String []args){
        EhcacheUtil util=EhcacheUtil.getInstance();
        //System.out.println(util.getClass().getResource("/ehcache/ehcache.xml"));
        util.put("people","yao","zhibin");
        System.out.println(util.get("people","yao"));
        util.remove("people","yao");
        System.out.println(util.get("people","yao"));
    }
}
