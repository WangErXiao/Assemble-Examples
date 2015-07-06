package ip;

import com.maxmind.geoip.LookupService;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by yao on 15/7/6.
 */
public class IPSearchService {

    public static void main(String[]args){
        IPSearchService ipSearchService=new IPSearchService();
        try {
            String dbfile =ipSearchService.getClass().getResource("/").getPath()+"ip/GeoLiteCity.dat";
            LookupService cl = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);

            InetAddress inetAddress = InetAddress.getByName("183.156.99.131");
            System.out.println(cl.getLocation(inetAddress).countryCode);
            System.out.println(cl.getLocation(inetAddress).area_code);
            System.out.println(cl.getLocation(inetAddress).city);
            System.out.println(cl.getLocation(inetAddress).region);
            System.out.println(cl.getLocation(inetAddress).dma_code);
            System.out.println(cl.getLocation("218.28.2.111").latitude);
            System.out.println(cl.getLocation("218.28.2.111").longitude);
            cl.close();
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        }



    }


}
