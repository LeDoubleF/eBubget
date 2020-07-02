package data;

import java.math.BigInteger;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.dao.CategoryEntity;
import data.dao.HibernateUtil;
import data.dto.CategoryDTo;

public class Repository {
	private static TreeSet<CategoryDTo> allCategory = new TreeSet<>();

	Repository() {
		throw new IllegalStateException("Utility class");
	}

	public static SortedSet<CategoryDTo> getAllCategory() {
		return allCategory;
	}

	public static void removeAllCategory() {
		allCategory.clear();

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
	}

	public static void initCategories() {
		if (allCategory.size() <= 1) {
			if (countCategoryEntity().equals(new BigInteger("0"))) {
				CategoryEntity.createCategories(".\\src\\main\\resources\\category.sql");// TODO paraméter le fichier
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

}
