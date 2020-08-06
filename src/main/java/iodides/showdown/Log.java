package iodides.showdown;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class Log {
	// 전역 로그

	public static Logger setConsoleLog() {
		//Logger log = Logger.getLogger(Log.class);
		Logger log = Logger.getRootLogger();
		try {
			String layout = "%d [%-15C{1}][%-5p] %m%n";

			PatternLayout patternlayout = new PatternLayout(layout);

			ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
			consoleAppender.setThreshold(Level.INFO);
		    log.addAppender(consoleAppender);
			
			//log.setLevel(Level.INFO);
		} catch (Exception e) {}

		return log;
	}

	// public static boolean setLog() {
	// 	//Logger log = Logger.getLogger(Log.class);
	// 	Logger log = Logger.getRootLogger();
	// 	try {
	// 		String layout = "%d [%-15C{1}][%-5p] %m%n";
	// 		PatternLayout patternlayout = new PatternLayout(layout);
			
	// 		String datePattern = ".yyyy-MM-dd";
	// 		DailyRollingFileAppender daumInfoAppender = new DailyRollingFileAppender(patternlayout, "log_daum.log", datePattern);
	// 		DailyRollingFileAppender torrentInfoAppender = new DailyRollingFileAppender(patternlayout, "log_torrent.log", datePattern);
	// 		DailyRollingFileAppender matchInfoAppender = new DailyRollingFileAppender(patternlayout, "log_match.log", datePattern);

	// 		daumInfoAppender.setThreshold(Level.INFO);
	// 		daumInfoAppender.setName("daumInfo");
	// 		daumInfoAppender.activateOptions();
	// 		log.addAppender(daumInfoAppender);
	// 		torrentInfoAppender.setThreshold(Level.INFO);
	// 		torrentInfoAppender.setName("torrentInfo");
	// 		torrentInfoAppender.activateOptions();
	// 		log.addAppender(torrentInfoAppender);
	// 		matchInfoAppender.setThreshold(Level.INFO);
	// 		matchInfoAppender.setName("matchInfo");
	// 		matchInfoAppender.activateOptions();
	// 		log.addAppender(matchInfoAppender);

	// 		DailyRollingFileAppender daumDebugAppender = new DailyRollingFileAppender(patternlayout, "debug_daum.log", datePattern);
	// 		DailyRollingFileAppender torrentDebugAppender = new DailyRollingFileAppender(patternlayout, "debug_torrent.log", datePattern);
	// 		DailyRollingFileAppender matchDebugAppender = new DailyRollingFileAppender(patternlayout, "debug_match.log", datePattern);

	// 		daumDebugAppender.setThreshold(Level.DEBUG);
	// 		daumDebugAppender.setName("daumDebug");
	// 		daumDebugAppender.activateOptions();
			
	// 		log.addAppender(daumDebugAppender);
	// 		torrentDebugAppender.setThreshold(Level.DEBUG);
	// 		torrentDebugAppender.setName("torrentDebug");
	// 		torrentDebugAppender.activateOptions();
	// 		log.addAppender(torrentDebugAppender);
	// 		matchDebugAppender.setThreshold(Level.DEBUG);
	// 		matchDebugAppender.setName("matchDebug");
	// 		matchDebugAppender.activateOptions();
	// 		log.addAppender(matchDebugAppender);
			
	// 		//ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
	// 		//consoleAppender.setThreshold(Level.INFO);
	// 	    //log.addAppender(consoleAppender);
	    	
	// 	    //log.setLevel(Level.DEBUG);
	// 	} catch (Exception e) {}
	// 	return true;
	// 	//return Logger.getLogger(appenderName);
	// }


	public static Logger setLog() {
		//Logger log = Logger.getLogger(Log.class);
		Logger log = Logger.getRootLogger();
		try {
			String layout = "%d [%-15C{1}][%-5p] %m%n";
			PatternLayout patternlayout = new PatternLayout(layout);
			
			String datePattern = ".yyyy-MM-dd";
			DailyRollingFileAppender infoFileAppender = new DailyRollingFileAppender(patternlayout, "logfile.log", datePattern);
			infoFileAppender.setThreshold(Level.INFO);
			log.addAppender(infoFileAppender);

			DailyRollingFileAppender debugFileAppender = new DailyRollingFileAppender(patternlayout, "debuglogfile.log", datePattern);
			debugFileAppender.setThreshold(Level.DEBUG);
			log.addAppender(debugFileAppender);
			
			ConsoleAppender consoleAppender = new ConsoleAppender(patternlayout);
			consoleAppender.setThreshold(Level.INFO);
			log.addAppender(consoleAppender);
	    	
		} catch (Exception e) {}

		return log;
	}
}