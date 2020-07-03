package ebudget;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
//	import java.io.IOException;
//	import java.util.logging.FileHandler;
//	import java.util.logging.Formatter;
//	import java.util.logging.Level;
//	import java.util.logging.Logger;
//	import java.util.logging.SimpleFormatter;

public class EBudgetLogger {
	// static private FileHandler fileTxt;
	// static private SimpleFormatter formatterTxt;
	//
	// static private FileHandler fileHTML;
	// static private Formatter formatterHTML;

	static public void setup() throws IOException {
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		// Logger logger = Logger.getLogger("logg");
		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler("C:\\Users\\ffazer\\Documents\\ebudget exe\\test\\logfile.log", true);
			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			logger.log(Level.WARNING, "Warning message");
			logger.log(Level.SEVERE, "Severe message");
			logger.log(Level.INFO, "Info message from Throwable object", new Throwable("Throwable exception"));
		} catch (SecurityException se) {
			System.out.println("SecurityException was catched " + se.getMessage());
		} catch (IOException ioe) {
			System.out.println("IOException was catchted " + ioe.getMessage());
		}
	}
	// // get the global logger to configure it
	// Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	//
	// // suppress the logging output to the console
	// Logger rootLogger = Logger.*getLogger*("");
	// Handler[] handlers = rootLogger.getHandlers();
	// if (handlers[0] instanceof ConsoleHandler) {
	// rootLogger.removeHandler(handlers[0]);
	// }
	//
	// logger.setLevel(Level.INFO);
	// fileTxt = new FileHandler("Logging.txt");
	// fileHTML = new FileHandler("Logging.html");
	//
	// // create a TXT formatter
	// formatterTxt = new SimpleFormatter();
	// fileTxt.setFormatter(formatterTxt);
	// logger.addHandler(fileTxt);
	//
	// // create an HTML formatter
	// formatterHTML = new MyHtmlFormatter();
	// fileHTML.setFormatter(formatterHTML);
	// logger.addHandler(fileHTML);
	// }
}
