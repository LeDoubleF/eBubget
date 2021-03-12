package ebudget;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FileResourcesUtils {

	public static void main(final String[] args) {

		final FileResourcesUtils app = new FileResourcesUtils();

		// TODO String fileName = "database.properties";
		final String fileName = "2.sql";

		System.out.println("getResourceAsStream : " + fileName);
		final InputStream is = app.getFileFromResourceAsStream(fileName);

		printInputStream(is);
		System.out.println("\ngetResource : " + fileName);
		File file = null;
		try {
			file = app.getFileFromResource(fileName);
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
		if (file != null) {
			printFile(file);
		}

	}

	// get a file from the resources folder
	// works everywhere, IDEA, unit test and JAR file.
	private InputStream getFileFromResourceAsStream(final String fileName) {

		// The class loader that loaded the class
		final ClassLoader classLoader = this.getClass()
			.getClassLoader();
		final InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}

	}

	/*
	 * The resource URL is not working in the JAR If we try to access a file
	 * that is inside a JAR, It throws NoSuchFileException (linux),
	 * InvalidPathException (Windows)
	 * 
	 * Resource URL Sample: file:java-io.jar!/json/file1.json
	 */
	private File getFileFromResource(final String fileName) throws URISyntaxException {

		final ClassLoader classLoader = this.getClass()
			.getClassLoader();
		final URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {

			// failed if files have whitespaces or special characters
			// return new File(resource.getFile());

			return new File(resource.toURI());
		}

	}

	// print input stream
	private static void printInputStream(final InputStream is) {

		try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(streamReader)) {

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	// print a file
	private static void printFile(final File file) {

		List<String> lines;
		try {
			lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			lines.forEach(System.out::println);
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}
