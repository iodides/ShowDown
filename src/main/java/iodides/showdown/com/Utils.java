package iodides.showdown.com;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static boolean daumFlag = true;
    public static boolean torrentFlag = true;
    public static boolean matchFlag = true;

    public static String daumBaseUrl(){
        return "https://search.daum.net/search";
    }

    public static void sleep(int interval) {
        try {
            Thread.sleep(1000 * interval);
        } catch (InterruptedException e) {

        }
    }

	public static String nextTime(int interval) {
        String result = "";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND,interval);
        result = sd.format(cal.getTime());
		return result;
    }

    public static String currentDate() {
        String result = "";
        SimpleDateFormat sd = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        result = sd.format(cal.getTime());
		return result;
    }
    
    
}