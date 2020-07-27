package ebudget.data.dao;

import java.io.Serializable;
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
import org.hibernate.Session;
import org.hibernate.Transaction;

import ebudget.data.Repository;
import ebudget.data.dto.CategoryDTo;

@Entity
@Table(name = "Category", uniqueConstraints = { @UniqueConstraint(columnNames = "NAME") })
public class CategoryEntity implements Serializable {

	private static final long serialVersionUID = -1798070786993154676L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Id
	@Column(name = "NAME", unique = true, nullable = false, length = 100)
	private String name;

	@Override
	public String toString() {
		return "CategoryEntity : " + name;
	}

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

	public static boolean save(String name) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		String stId = null;
		try {

			tx = session.beginTransaction();
			CategoryEntity category = new CategoryEntity(name);
			stId = (String) session.save(category);
			tx.commit();
			Repository.addCategory(new CategoryDTo(name));
			LOGGER.log(Level.INFO, "Création de la catégorie :" + name);
		} catch (HibernateException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
			if (tx != null)
				tx.rollback();
		} finally {
			session.close();
		}

		return stId != null;
	}
	/* private String getAbsolutePath(String resourceName) { // TODO acceder a
	 * ressource // ClassLoader classLoader = getClass().getClassLoader(); try { //
	 * returns the Class object associated with this class Class cls =
	 * Class.forName("ebudget.Ebudget");
	 * 
	 * // returns the ClassLoader object associated with this Class. ClassLoader
	 * classLoader = cls.getClassLoader();
	 * 
	 * if (classLoader == null) {
	 * System.out.println("The default system class was used."); } else { // returns
	 * the class loader
	 * 
	 * System.out.println("Class associated with ClassLoader = " +
	 * classLoader.toString()); File file = new
	 * File(classLoader.getResource(resourceName).getFile()); String absolutePath =
	 * file.getAbsolutePath(); } } catch (ClassNotFoundException e) {
	 * System.out.println(e.toString()); }
	 * 
	 * return " absolutePath"; } */
}
