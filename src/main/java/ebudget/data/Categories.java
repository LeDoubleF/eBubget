package ebudget.data;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ebudget.data.dao.CategoryEntity;
import ebudget.data.dao.HibernateUtil;
import ebudget.data.dto.CategoryDto;
import ebudget.io.CSVReader;

/**
 * Permet de connaitre et de g�rer les cat�gories existantes
 * 
 * @author ffazer
 *
 */
public class Categories {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static TreeSet<CategoryDto> allCategory = new TreeSet<>();

	private static CategoryDto defaultCategory = intDefaultCategory();

	Categories() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * R�cupere la categorie par d�faut d�nit dans le fichier de properties
	 *
	 * @return cat�gorie par d�faut
	 */
	public static CategoryDto getDefaultCategory() {
		return defaultCategory;
	}

	public static SortedSet<CategoryDto> getAllCategory() {
		return allCategory;
	}

	public static int countCategory() {
		return allCategory.size();
	}

	public static boolean isCategory(CategoryDto category) {

		return allCategory.contains(category);
	}

	public static boolean isIncome(CategoryDto category) {
		if (allCategory.contains(category))
			return category.isIncome();
		else
			return false;
	}

	public static void addCategory(CategoryDto category) {
		allCategory.add(category);
	}

	public static void setDefaultCategory(CategoryDto defaultCategory) {
		Categories.defaultCategory = defaultCategory;
	}

	public static void clearAllCategory() {
		allCategory.clear();
	}

	public static void addCategories(List<CategoryDto> categoriesList) {
		categoriesList.forEach(item -> allCategory.add(item));
		LOGGER.log(Level.INFO, "ajout multiple dans la listes des cat�gories");
	}

	public static void initCategories() {

		allCategory.add(defaultCategory);
		if (allCategory.size() <= 1) {
			if (countCategoryEntity().equals(BigInteger.valueOf(0))) {
				try {
					createCategories("Categories.csv");
					CategoryEntity.save(defaultCategory.getName());
				} catch (Exception e) {
					System.out.println(e.getMessage());
					LOGGER.log(Level.SEVERE, e.getMessage());
					e.printStackTrace();
				}

			} else {
				Session session = HibernateUtil.getSessionFactory()
					.openSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					selectAllCategories(session);

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

	private static CategoryDto intDefaultCategory() {
		// TODO voir importExportIntegrationTest pour la gestion des properties
		// TODO a quoi sert data-spring-config.xml?
		ResourceBundle bundle = ResourceBundle.getBundle("ebudget.properties.config");
		return new CategoryDto(bundle.getString("app.DefaultCategory"));

	}

	/**
	 * count category in data base
	 *
	 * @return
	 */
	protected static BigInteger countCategoryEntity() {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query<BigInteger> query = session.createSQLQuery("SELECT count(*) FROM CATEGORY");
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

	@SuppressWarnings("unchecked")
	private static void createCategories(String fileName) {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();

		Transaction tx = null;
		try {
			// TODO acceder au fichier par parametrage � l'installation
			CSVReader csvReader = new CSVReader();
			List<CategoryDto> categoryList = csvReader.readCategoriesFile(fileName);

			tx = session.beginTransaction();

			for (CategoryDto category : categoryList) {
				CategoryEntity.save(category);
			}

			selectAllCategories(session);
			session.getTransaction()
				.commit();
			LOGGER.log(Level.INFO, "Cr�ation des cat�gories � partir du fichier {0}", fileName);

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}

	}

	protected static void selectAllCategories(Session session) {
		String hql = "FROM CategoryEntity";
		Query<CategoryEntity> query = session.createQuery(hql);
		@SuppressWarnings("unchecked")
		List<CategoryEntity> listCategories = query.list();

		for (CategoryEntity category : listCategories) {
			allCategory.add(new CategoryDto(category.getName(), category.getIncome()));
		}

	}

	public static CategoryDto getCategory(String categoryName) {
		CategoryDto category = new CategoryDto(categoryName);
		if (Categories.isCategory(category))
			return category;
		else {
			category.setIncome(true);
			if (Categories.isCategory(category))
				return category;
			return null;
		}

	}

}