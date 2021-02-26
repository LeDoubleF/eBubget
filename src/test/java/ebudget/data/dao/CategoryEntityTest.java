package ebudget.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.data.Common;
import ebudget.data.dto.CategoryDto;

class CategoryEntityTest {

	static Logger logger = Logger.getLogger("CategoryEntityTest");

	@BeforeEach
	public void clean() {
		Common.clearDataBase();
	}

	@AfterAll
	public static void cleanUp() {
		Common.clearDataBase();
	}

	@Test
	void testSaveCategoryinLowerCase() {

		Session sessionOne = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = sessionOne.beginTransaction();
		CategoryEntity category = new CategoryEntity();
		category.setName("LoKeesh");

		sessionOne.save(category);
		String catName = category.getName();
		tx.commit();

		Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
		sessionTwo.beginTransaction();

		CategoryEntity cat1 = (CategoryEntity) sessionTwo.load(CategoryEntity.class, catName);

		assertEquals(catName.toLowerCase(), cat1.getName());
	}

	@Test
	void testSaveCategory() {

		Session sessionOne = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = sessionOne.beginTransaction();
		CategoryEntity category = new CategoryEntity();
		category.setName("oeesh");

		sessionOne.save(category);
		String catName = category.getName();
		tx.commit();

		Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
		sessionTwo.beginTransaction();

		CategoryEntity cat1 = (CategoryEntity) sessionTwo.load(CategoryEntity.class, catName);

		assertEquals(catName.toLowerCase(), cat1.getName());
		assertEquals("CategoryEntity : oeesh", cat1.toString());
	}

	@Test
	void testCategoryOtherConstructor() {

		Session sessionOne = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = sessionOne.beginTransaction();
		CategoryEntity category = new CategoryEntity(new CategoryDto("LoKeesh"));

		sessionOne.save(category);
		String catName = category.getName();
		tx.commit();

		Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
		sessionTwo.beginTransaction();

		CategoryEntity cat1 = (CategoryEntity) sessionTwo.load(CategoryEntity.class, catName);

		assertEquals(catName.toLowerCase(), cat1.getName());

	}
}