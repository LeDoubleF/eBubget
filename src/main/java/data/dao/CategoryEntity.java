package data.dao;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.Repository;

@Entity
@Table(name = "Category", uniqueConstraints = { @UniqueConstraint(columnNames = "NAME") })
public class CategoryEntity implements Serializable {

	@Override
	public String toString() {
		return "CategoryEntity : " + name;
	}

	private static final long serialVersionUID = -1798070786993154676L;
	static Logger logger = Logger.getLogger("CategoryEntity");

	@Id
	@Column(name = "NAME", unique = true, nullable = false, length = 100)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toLowerCase();
	}

	// Hibernate requires no-args constructor
	public CategoryEntity() {
	}

	public CategoryEntity(String name) {
		this.name = name.toLowerCase();
	}

	@OneToMany(mappedBy = "category")
	private Set<TransactionEntity> transactions;

	// TODO vider catégory avec destruction en cascade des transactions ou
	// remplacement par divers
	public static void createCategories(String sqlFile) {
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

			Repository.addCategories(queryResult.list());

			session.getTransaction().commit();
			logger.log(Level.INFO, "création des catégories");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "File Not Found", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}

	}

	public static void deleteAllCategory() {
		Transaction tx = null;
		try {

			Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("DELETE FROM category");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction().commit();
			logger.log(Level.INFO, "suppression des catégories");
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}

	}

	public static boolean save(String name) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Integer stId = null;
		try {

			tx = session.beginTransaction();
			CategoryEntity category = new CategoryEntity(name);
			stId = (Integer) session.save(category);
			tx.commit();
		} catch (HibernateException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			if (tx != null)
				tx.rollback();
		} finally {
			session.close();
		}

		return stId != null;
	}
	// private String getAbsolutePath(String resourceName) {
	// // TODO acceder a ressource
	// // ClassLoader classLoader = getClass().getClassLoader();
	// try {
	// // returns the Class object associated with this class
	// Class cls = Class.forName("ebudget.Ebudget");
	//
	// // returns the ClassLoader object associated with this Class.
	// ClassLoader classLoader = cls.getClassLoader();
	//
	// if (classLoader == null) {
	// System.out.println("The default system class was used.");
	// } else {
	// // returns the class loader
	//
	// System.out.println("Class associated with ClassLoader = " +
	// classLoader.toString());
	// File file = new File(classLoader.getResource(resourceName).getFile());
	// String absolutePath = file.getAbsolutePath();
	// }
	// } catch (ClassNotFoundException e) {
	// System.out.println(e.toString());
	// }
	//
	// return " absolutePath";
	// }
}
