package ebudget;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class EBudgetLogger {

	private static String directory = ".//logs";

	public static void setup() throws IOException {

		// get the global logger to configure it
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		// suppress the logging output to the console
		Logger rootLogger = Logger.getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		if (handlers[0] instanceof ConsoleHandler) {
			rootLogger.removeHandler(handlers[0]);
		}

		logger.setLevel(Level.INFO);

		if (!new File(directory).exists()) {
			// Créer le dossier avec tous ses parents
			new File(directory).mkdirs();

		}

		FileHandler fileTxt = new FileHandler(".//logs//Logging.txt");
		FileHandler fileHTML = new FileHandler(".//logs//Logging.html");

		// create a TXT formatter
		SimpleFormatter formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);

		// create an HTML formatter
		Formatter formatterHTML = new MyHtmlFormatter();
		fileHTML.setFormatter(formatterHTML);
		logger.addHandler(fileHTML);
	}

	private EBudgetLogger() {
		throw new IllegalStateException("Utility class");
	}

}
