package ebudget.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.data.dao.CategoryEntity;
import ebudget.data.dto.CategoryDto;

class CategorieTest {

	private static final Integer NBCategories = 39;

	@BeforeEach
	public void clean() {
		Common.clearDataBase();
	}

	@AfterAll
	public static void cleanUp() {
		Common.clearDataBase();
	}

	@Test
	void initCategoryWithCategory() {
		CategoryDto category = new CategoryDto("stuf");
		Categories.addCategory(category);
		Categories.initCategories();
		assertEquals(2, Categories.countCategory());
		assertTrue(Categories.isCategory(category));
	}

	@Test
	void initCategoryWithSQLFile() {
		Categories.initCategories();
		assertEquals(NBCategories, Categories.countCategory());
	}

	@Test
	void initCategoryWithDatabaseNotEmpty() {
		CategoryEntity.save("toto");
		Categories.clearAllCategory();
		Categories.initCategories();
		assertEquals(2, Categories.countCategory());
	}

	@Test
	void testDefaultCategory() {
		CategoryDto defaultCategory = Categories.getDefaultCategory();
		assertEquals(new CategoryDto("Divers"), defaultCategory);
	}

	@Test
	void testConstructorCategory() {
		try {
			Categories cat = new Categories();
			fail("should throw exception");
		} catch (IllegalStateException aExp) {
			assertTrue(aExp.getMessage()
				.contains("Utility class"));
		}
	}

	@Test
	void testGetCategory() {
		Categories.clearAllCategory();
		assertEquals(null, Categories.getCategory("toto"));

		CategoryDto category1 = new CategoryDto("stuf", false);
		Categories.addCategory(category1);
		assertEquals(category1, Categories.getCategory("stuf"));

		CategoryDto category2 = new CategoryDto("bobo", true);
		Categories.addCategory(category2);
		assertEquals(category2, Categories.getCategory("bobo"));

		assertEquals(null, Categories.getCategory("toto22"));

	}
}
