package utils;

import sun.plugin2.main.client.CALayerProvider;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yao on 15/7/1.
 */
public class DateUtils {

    public static void main(String[]args){

        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.HOUR, -16);
        Date date=calendar.getTime();
        System.out.println(date.getHours());
        System.out.println(date.getMinutes());
        int section=date.getHours()*2;
        section+=1;
        if(date.getMinutes()>30){
            section+=1;
        }
        System.out.println(section);
        System.out.println(Double.parseDouble(""));
    }
}
