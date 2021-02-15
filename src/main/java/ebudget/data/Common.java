package ebudget.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ebudget.data.dao.CategoryEntity;
import ebudget.data.dao.HibernateUtil;
import ebudget.data.dao.PeriodEntity;
import ebudget.data.dao.RecurringExpensesEntity;
import ebudget.data.dao.TransactionEntity;

public class Common {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	Common() {
		throw new IllegalStateException("Utility class");
	}

	/***************************************************************************/

	public static void loadForecats(String sqlFile) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		try {
			// TODO acceder au fichier par parametrage à l'installation

			File file = new File(sqlFile);

			String sqlScript = null;
			sqlScript = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).trim().toLowerCase();

			tx = session.beginTransaction();

			Query query = session.createSQLQuery(sqlScript);
			query.executeUpdate();

			session.getTransaction().commit();
			LOGGER.log(Level.INFO, "création des prévisions");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "File Not Found", e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}

	}

	/**
	 * supprime toutes les catégories et rajoute divers
	 */
	public static void deleteAllCategory() {
		Transaction tx = null;
		try {
			// TODO suppression cascade
			TransactionEntity.deleteAll();
			RecurringExpensesEntity.deleteAll();

			Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("DELETE FROM category");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction().commit();
			LOGGER.log(Level.INFO, "suppression de toutes les catégories de la base de donnée");
			Categories.clearAllCategory();
			LOGGER.log(Level.INFO, "suppression de toutes les catégories de la liste");
			CategoryEntity.save("divers");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "erreur lors de la suppression des catégories et rajout de divers", e);
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}
	}

	public static void clearDataBase() {
		PeriodEntity.deleteAll();
		deleteAllCategory();
	}

	private static ArrayList<Double> calulateBalanceByMonth() {
		Transaction tx = null;
		ArrayList<Double> balanceByMonth = new ArrayList<>();
		ArrayList<String> month = new ArrayList<>();
		month.add("january");
		month.add("february");
		month.add("march");
		month.add("april");
		month.add("may");
		month.add("june");
		month.add("july");
		month.add("august");
		month.add("september");
		month.add("october");
		month.add("november");
		month.add("december");
		for (String thisMonth : month) {
			try {
				// TODO requete apparait deux fois dans les log
				Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
				tx = sessionTwo.beginTransaction();

				Query queryIncome = sessionTwo.createSQLQuery("SELECT SUM(amount) FROM forecast WHERE " + thisMonth + "=1 and income =TRUE;");

				Double incomListResult = (Double) queryIncome.list().get(0);
				Double amountIncome = incomListResult == null ? 0 : (Double) incomListResult;

				Query queryExpense = sessionTwo.createSQLQuery("SELECT SUM(amount) FROM forecast WHERE " + thisMonth + "=1 and income =FALSE;");

				Double expenseList = (Double) queryExpense.list().get(0);
				Double amountExpense = expenseList == null ? 0 : expenseList;

				balanceByMonth.add(amountIncome - amountExpense);

			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "erreur lors du calcul du solde du mois de {0} {1}", new Object[]{thisMonth, e});
				if (tx != null)
					tx.rollback();
			}
		}
		return balanceByMonth;
	}

}
