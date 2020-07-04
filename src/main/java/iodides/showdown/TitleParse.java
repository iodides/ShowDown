package iodides.showdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleParse {

    public static String getName(String title) {
		return title.split("\\.")[0];
	}
	
	public static int[] getEpi(String title) {
		String epi1 = "0";
		String epi2 = "0";
		
		if(title.matches(".*[0-9][0-9]-[0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9][0-9]-[0-9][0-9]회").replace("회", "");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		}else if(title.matches(".*[0-9][0-9] [0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9][0-9] [0-9][0-9]회").replace("회", "");
			epi1 = temp.split(" ")[0];
			epi2 = temp.split(" ")[1];
		}else if(title.matches(".*[0-9]-[0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9]-[0-9][0-9]회").replace("회", "");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		}else if(title.matches(".*[0-9] [0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9] [0-9][0-9]회").replace("회", "");
			epi1 = temp.split(" ")[0];
			epi2 = temp.split(" ")[1];	
		}else if(title.matches(".*[0-9]-[0-9]회.*")) {
			String temp = pmatch(title, "[0-9]-[0-9]회").replace("회", "");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		}else if(title.matches(".*[0-9] [0-9]회.*")) {
			String temp = pmatch(title, "[0-9] [0-9]회").replace("회", "");
			epi1 = temp.split(" ")[0];
			epi2 = temp.split(" ")[1];	
		}else if(title.matches(".*[0-9][0-9]-[0-9][0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9][0-9]-[0-9][0-9][0-9]회").replace("회", "");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		}else if(title.matches(".*[0-9][0-9] [0-9][0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9][0-9] [0-9][0-9][0-9]회").replace("회", "");
			epi1 = temp.split(" ")[0];
			epi2 = temp.split(" ")[1];	
		}else if(title.matches(".*E[0-9][0-9]E[0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9]E[0-9][0-9]");
			epi1 = temp.substring(0, 3);
			epi2 = temp.substring(3, 6);	
		}else if(title.matches(".*E[0-9][0-9]-E[0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9]-E[0-9][0-9]");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		}else if(title.matches(".*E[0-9][0-9]~E[0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9]~E[0-9][0-9]");
			epi1 = temp.split("~")[0];
			epi2 = temp.split("~")[1];
		}else if(title.matches(".*E[0-9][0-9]~E[0-9][0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9]~E[0-9][0-9][0-9]");
			epi1 = temp.split("~")[0];
			epi2 = temp.split("~")[1];
		}else if(title.matches(".*E[0-9][0-9][0-9]~E[0-9][0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9][0-9]~E[0-9][0-9][0-9]");
			epi1 = temp.split("~")[0];
			epi2 = temp.split("~")[1];	
		}else if(title.matches(".*[eE][0-9][0-9]\\.[eE][0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9].E[0-9][0-9]");
			epi1 = temp.split("\\.")[0];
			epi2 = temp.split("\\.")[1];
		}else if(title.matches(".*[eE][0-9][0-9][0-9][0-9].*")) {
			String temp = pmatch(title, "[eE][0-9][0-9][0-9][0-9]");
			epi1 = temp;	
		}else if(title.matches(".*[eE][0-9][0-9][0-9].*")) {
			String temp = pmatch(title, "[eE][0-9][0-9][0-9]");
			epi1 = temp;
		}else if(title.matches(".*[eE][0-9][0-9].*")) {
			String temp = pmatch(title, "[eE][0-9][0-9]");
			epi1 = temp;
		}
		
		epi1 = epi1.replaceAll("[^0-9]", "");
		epi2 = epi2.replaceAll("[^0-9]", "");
		
		int[] i_array = new int[2];
		i_array[0] = Integer.parseInt(epi1);
		i_array[1] = Integer.parseInt(epi2);
		
		return i_array;
		
	}
	
	public static String getAirDate(String title) {
		return pmatch(title, "\\.[0-9][0-9][0-9][0-9][0-9][0-9]\\.").replace(".",  "");
	}
	
	public static String getQuality(String title) {
		String result = "";

		if(title.matches(".*1080[pP].*")) result = "FHD";
		else if(title.matches(".*720[pP].*")) result = "HD";

		return result;
	}
	
	// 패턴에 일치하는 문자열만 추출
	public static String pmatch(String str, String ptn) {
		String result = "";
		Pattern p = Pattern.compile(ptn);
		Matcher m = p.matcher(str);
		while(m.find()) {
			result = m.group(0);	
		}
		return result;
	}

	public static String getRelGroup(String title) {
		return "";
	}
    
}