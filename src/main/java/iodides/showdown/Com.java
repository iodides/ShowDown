package iodides.showdown;

public class Com {

    public static void sleep(String time) {
        int sec = 0;
        if (time.contains("s")) {
            sec = Integer.parseInt(time.replace("s", ""));
        } else if (time.contains("m")) {
            sec = Integer.parseInt(time.replace("m", "")) * 60;
        } else if (time.contains("h")) {
            sec = Integer.parseInt(time.replace("h", "")) * 60 * 60;
        }

        try {
            Thread.sleep(1000 * sec);
        } catch (InterruptedException e) {
        }
    }
    
}