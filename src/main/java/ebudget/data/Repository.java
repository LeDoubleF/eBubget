package ebudget.data;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ebudget.data.dao.CategoryEntity;
import ebudget.data.dao.ForecastEntity;
import ebudget.data.dao.HibernateUtil;
import ebudget.data.dao.PeriodEntity;
import ebudget.data.dao.TransactionEntity;
import ebudget.data.dto.CategoryDTo;

public class Repository {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	protected static TreeSet<CategoryDTo> allCategory = new TreeSet<>();

	Repository() {
		throw new IllegalStateException("Utility class");
	}

	public static SortedSet<CategoryDTo> getAllCategory() {
		return allCategory;
	}

	public static int countCategory() {
		return allCategory.size();
	}

	public static boolean isCategory(CategoryDTo category) {
		return allCategory.contains(category);
	}

	public static void addCategory(CategoryDTo category) {
		allCategory.add(category);
	}

	public static void addCategories(List<String> categories) {
		categories.forEach(item -> allCategory.add(new CategoryDTo(item)));
		LOGGER.log(Level.INFO, "ajout multiple dans la listes des catégories");
	}

	public static void initCategories() {
		if (allCategory.size() <= 1) {
			if (countCategoryEntity().equals(new BigInteger("0"))) {
				createCategories(".\\src\\main\\resources\\category.sql");// TODO paraméter le fichier
			} else {
				Session session = HibernateUtil.getSessionFactory().openSession();
				Transaction tx = null;
				try {
					CategoryDTo divers = new CategoryDTo("divers");
					allCategory.add(divers);
					tx = session.beginTransaction();
					Query query = session.createSQLQuery("SELECT name FROM CATEGORY");
					@SuppressWarnings("unchecked")
					List<String> categories = query.list();
					Repository.addCategories(categories);
				} catch (RuntimeException e) {
					if (tx != null)
						tx.rollback();
					throw e;
				} finally {
					session.close();
				}
			}
		}

	}

	/**
	 * count category in data base
	 * 
	 * @return
	 */
	protected static BigInteger countCategoryEntity() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT count(*) FROM CATEGORY");
			@SuppressWarnings("unchecked")
			List<BigInteger> count = query.list();
			return count.get(0);
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			session.close();
		}

	}

	private static void createCategories(String sqlFile) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		try {
			// String SqlFile = getAbsolutePath("category.sql");
			// TODO acceder au fichier par parametrage à l'installation

			File file = new File(sqlFile);

			String sqlScript = null;
			sqlScript = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).trim().toLowerCase();

			tx = session.beginTransaction();

			Query query = session.createSQLQuery(sqlScript);
			query.executeUpdate();

			Query queryResult = session.createSQLQuery("SELECT name FROM category");
			queryResult.executeUpdate();

			@SuppressWarnings({ "unchecked" })
			List<String> categories = queryResult.list();

			Repository.addCategories(categories);

			session.getTransaction().commit();
			LOGGER.log(Level.INFO, "création des catégories");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "File Not Found", e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}

	}

	public static void loadForecats(String sqlFile) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		try {
			// String SqlFile = getAbsolutePath("category.sql");
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
			ForecastEntity.deleteAll();

			Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("DELETE FROM category");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction().commit();
			LOGGER.log(Level.INFO, "suppression de toutes les catégories de la base de donnée");
			allCategory.clear();
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
		Repository.deleteAllCategory();
	}
}
