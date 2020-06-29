package ebudget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

// this custom formatter formats parts of a log record to a single line
class MyHtmlFormatter extends Formatter {

	private static final String TD_CLOSE = "</td>\n";
	private static final String TD = "\t<td>";

	/**
	 * this method is called for every log records
	 */
	public String format(LogRecord rec) {
		StringBuilder buf = new StringBuilder(1000);
		buf.append("<tr>\n");

		// colorize any levels >= WARNING in red
		if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
			String color = (rec.getLevel().intValue() == Level.WARNING.intValue()) ? "\t<td style=\"color:orange\">" : "\t<td style=\"color:red\">";

			buf.append(color);
			buf.append("<b>");
			buf.append(rec.getLevel());
			buf.append("</b>");
		} else {
			buf.append(TD);
			buf.append(rec.getLevel());
		}

		buf.append(TD_CLOSE);
		buf.append(TD);
		buf.append(calcDate(rec.getMillis()));
		buf.append(TD_CLOSE);
		buf.append(TD);
		buf.append(formatMessage(rec));
		buf.append(TD_CLOSE);
		buf.append(TD);
		buf.append(rec.getSourceClassName());
		buf.append(TD_CLOSE);
		buf.append(TD);
		buf.append(rec.getSourceMethodName());
		buf.append(TD_CLOSE);
		buf.append("</tr>\n");

		return buf.toString();

	}

	private String calcDate(long millisecs) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		Date resultdate = new Date(millisecs);
		return dateFormat.format(resultdate);
	}

	/**
	 * this method is called just after the handler using this formatter is
	 * created
	 */
	@Override
	public String getHead(Handler h) {
		return "<!DOCTYPE html>\n<head>\n<style>\n" + "table { width: 100% }\n" + "th { font:bold 10pt Tahoma; }\n"
				+ "td { font:normal 10pt Tahoma; }\n" + "h1 {font:normal 11pt Tahoma;}\n" + "</style>\n" + "</head>\n" + "<body>\n" + "<h1>"
				+ (new Date()) + "</h1>\n" + "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\">\n" + "<tr align=\"left\">\n"
				+ "\t<th style=\"width:10%\">Loglevel</th>\n" + "\t<th style=\"width:15%\">Time</th>\n"
				+ "\t<th style=\"width:75%\">Log Message</th>\n" + "\t<th style=\"width:75%\">FILE</th>\n" + "\t<th style=\"width:75%\">Method</th>\n"
				+ "</tr>\n";
	}

	/**
	 * this method is called just after the handler using this formatter is
	 * closed
	 */
	@Override
	public String getTail(Handler h) {
		return "</table>\n</body>\n</html>";
	}
}
