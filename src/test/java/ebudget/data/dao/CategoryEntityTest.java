package ebudget.data.dao;

import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import ebudget.data.Repository;
import junit.framework.TestCase;

public class CategoryEntityTest extends TestCase {
	static Logger logger = Logger.getLogger("CategoryEntityTest");

	@Test
	public void testSaveCategoryinLowerCase() {
		Repository.deleteAllCategory();

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
		Repository.deleteAllCategory();
	}

	// TODO nettoyer la table apres chaque test
}