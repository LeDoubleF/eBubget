package exception;

public final class Message {

	private Message() {
		throw new IllegalStateException("Utility class");
	}

	public static final String FILE_PATH_KO = "Invalid file path";
	public static final String FILE_EXTENSION_KO = "Invalid file extension";
	public static final String NO_FILE = "There is no file";
	public static final String FILE_CONTENT_KO = "File content error";
	public static final String UITLITY_CLASS = "Utility class";
	public static final String INVALD_MONTH = "Invalid number of month";
}
