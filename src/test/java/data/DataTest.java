package data;

import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.dao.HibernateUtil;

public class DataTest {

	static Logger logger = Logger.getLogger("DataTest");

	public static void deleteCategory() {
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();
			Query queryDelete = session.createSQLQuery("DELETE FROM category");
			queryDelete.executeUpdate();

			session.getTransaction().commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e; // or display error message
		} finally {
			session.close();
		}

	}

	public static void deleteTransaction() {
		Transaction tx = null;
		try {

			Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("DELETE FROM transaction");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction().commit();
		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
		}
	}

	public static void deletePeriode() {
		Transaction tx = null;
		try {

			Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("DELETE FROM periode");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction().commit();
		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
		}
	}

}
