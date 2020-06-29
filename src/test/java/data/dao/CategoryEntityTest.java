package data.dao;

import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import data.DataTest;
import junit.framework.TestCase;

public class CategoryEntityTest extends TestCase {
	static Logger logger = Logger.getLogger("CategoryEntityTest");

	@Test
	public void testSaveCategoryinLowerCase() {
		DataTest.deleteTransaction();
		DataTest.deleteCategory();

		Session sessionOne = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = sessionOne.beginTransaction();
		CategoryEntity category = new CategoryEntity();
		category.setName("LoKeesh");

		sessionOne.save(category);
		String catName = category.getName();
		sessionOne.getTransaction().commit();

		Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
		sessionTwo.beginTransaction();

		CategoryEntity cat1 = (CategoryEntity) sessionTwo.load(CategoryEntity.class, catName);

		assertEquals(catName.toLowerCase(), cat1.getName());
		CategoryEntity.deleteAllCategory();
	}

	@Test
	public void testCreateCategories() {
		CategoryEntity.createCategories("C:\\Donnees\\Perso\\projet\\budget\\eBudget\\category.sql");

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		CategoryEntity cat1 = (CategoryEntity) session.load(CategoryEntity.class, "alimentation");
		assertEquals(cat1.getName(), "alimentation");

		CategoryEntity cat2 = (CategoryEntity) session.load(CategoryEntity.class, "credit");
		assertEquals(cat2.getName(), "credit");

		CategoryEntity cat3 = (CategoryEntity) session.load(CategoryEntity.class, "vacances");
		assertEquals(cat3.getName(), "vacances");
		CategoryEntity.deleteAllCategory();

	}

	// @Override
	// TODO nettoyer la table apres chaque test
	// protected void tearDown() {
	//
	// System.out.println("Running: tearDown");
	// Session session = HibernateUtil.getSessionFactory().openSession();
	// session.beginTransaction();
	// // Query queryDelete = session.createSQLQuery("DROP TABLE IF EXISTS
	// category");
	// Query queryDelete = session.createSQLQuery("DELETE FROM category");
	// queryDelete.executeUpdate();
	// HibernateUtil.shutdown();
	// }
}