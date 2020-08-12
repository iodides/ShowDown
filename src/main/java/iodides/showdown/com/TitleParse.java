package iodides.showdown.com;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import iodides.showdown.DB;

public class TitleParse {

	public static String getName(String title) {
		// 문자열 비교를 위해 특수문자 제거, 공백제거, 소문자화
		String temp = title.split("\\.")[0].replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "").replaceAll(" ","").toLowerCase();
		temp = temp.replaceAll("  "," ");
		temp = temp.replaceAll("  "," ");
		return temp;
	}

	public static int[] getEpi(String title) {
		String epi1 = "0";
		String epi2 = "0";

		if (title.matches(".*[0-9][0-9]-[0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9][0-9]-[0-9][0-9]회").replace("회", "");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		} else if (title.matches(".*[0-9][0-9] [0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9][0-9] [0-9][0-9]회").replace("회", "");
			epi1 = temp.split(" ")[0];
			epi2 = temp.split(" ")[1];
		} else if (title.matches(".*[0-9]-[0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9]-[0-9][0-9]회").replace("회", "");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		} else if (title.matches(".*[0-9] [0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9] [0-9][0-9]회").replace("회", "");
			epi1 = temp.split(" ")[0];
			epi2 = temp.split(" ")[1];
		} else if (title.matches(".*[0-9]-[0-9]회.*")) {
			String temp = pmatch(title, "[0-9]-[0-9]회").replace("회", "");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		} else if (title.matches(".*[0-9] [0-9]회.*")) {
			String temp = pmatch(title, "[0-9] [0-9]회").replace("회", "");
			epi1 = temp.split(" ")[0];
			epi2 = temp.split(" ")[1];
		} else if (title.matches(".*[0-9][0-9]-[0-9][0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9][0-9]-[0-9][0-9][0-9]회").replace("회", "");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		} else if (title.matches(".*[0-9][0-9] [0-9][0-9][0-9]회.*")) {
			String temp = pmatch(title, "[0-9][0-9] [0-9][0-9][0-9]회").replace("회", "");
			epi1 = temp.split(" ")[0];
			epi2 = temp.split(" ")[1];
		} else if (title.matches(".*E[0-9][0-9]E[0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9]E[0-9][0-9]");
			epi1 = temp.substring(0, 3);
			epi2 = temp.substring(3, 6);
		} else if (title.matches(".*E[0-9][0-9]-E[0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9]-E[0-9][0-9]");
			epi1 = temp.split("-")[0];
			epi2 = temp.split("-")[1];
		} else if (title.matches(".*E[0-9][0-9]~E[0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9]~E[0-9][0-9]");
			epi1 = temp.split("~")[0];
			epi2 = temp.split("~")[1];
		} else if (title.matches(".*E[0-9][0-9]~E[0-9][0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9]~E[0-9][0-9][0-9]");
			epi1 = temp.split("~")[0];
			epi2 = temp.split("~")[1];
		} else if (title.matches(".*E[0-9][0-9][0-9]~E[0-9][0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9][0-9]~E[0-9][0-9][0-9]");
			epi1 = temp.split("~")[0];
			epi2 = temp.split("~")[1];
		} else if (title.matches(".*[eE][0-9][0-9]\\.[eE][0-9][0-9].*")) {
			String temp = pmatch(title, "E[0-9][0-9].E[0-9][0-9]");
			epi1 = temp.split("\\.")[0];
			epi2 = temp.split("\\.")[1];
		} else if (title.matches(".*[eE][0-9][0-9][0-9][0-9].*")) {
			String temp = pmatch(title, "[eE][0-9][0-9][0-9][0-9]");
			epi1 = temp;
		} else if (title.matches(".*[eE][0-9][0-9][0-9].*")) {
			String temp = pmatch(title, "[eE][0-9][0-9][0-9]");
			epi1 = temp;
		} else if (title.matches(".*[eE][0-9][0-9].*")) {
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

	public static String getAir(String title) {
		return pmatch(title, "\\.[0-9][0-9][0-9][0-9][0-9][0-9]\\.").replace(".", "");
	}

	public static String getQuality(String title) {
		String result = "";

		if (title.matches(".*1080[pP].*"))
			result = "FHD";
		else if (title.matches(".*720[pP].*"))
			result = "HD";

		return result;
	}

	// 패턴에 일치하는 문자열만 추출
	public static String pmatch(String str, String ptn) {
		String result = "";
		Pattern p = Pattern.compile(ptn);
		Matcher m = p.matcher(str);
		while (m.find()) {
			result = m.group(0);
		}
		return result;
	}

	public static String getRelGroup(String title) {
		try {
			ArrayList<String> relGroups = DB.getRelGroups();
			for(String relGroup : relGroups){
				if(title.toLowerCase().contains(relGroup.toLowerCase())) return relGroup;
			}
		} catch (SQLException e) {
			
		}
		return "";
	}
    
}