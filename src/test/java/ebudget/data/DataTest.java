package ebudget.data;

import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ebudget.data.dao.HibernateUtil;

public class DataTest {

	static Logger logger = Logger.getLogger("DataTest");

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

}
