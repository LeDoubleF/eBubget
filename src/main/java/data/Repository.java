package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.dto.CategoryDTo;

public class Repository {
	private static List<CategoryDTo> allCategory = new ArrayList<CategoryDTo>(Arrays.asList(new CategoryDTo("divers")));

	public static List<CategoryDTo> getAllCategory() {
		return allCategory;
	}

	public static boolean isCategory(CategoryDTo category) {
		return allCategory.contains(category);
	}

	public static void addCategory(CategoryDTo category) {
		allCategory.add(category);
	}

	public static void addCategories(List<String> categories) {
		categories.forEach(item -> allCategory.add(new CategoryDTo(item)));
	}

}
