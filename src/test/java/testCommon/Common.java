package testCommon;
import java.io.File;

public class Common {
	public static String getAbsolutePath(String resourceName, ClassLoader classLoader) {
		File file = new File(classLoader.getResource(resourceName).getFile());
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}
}
