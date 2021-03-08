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
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;

@Entity
@Table(name = "Category", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME")})
public class CategoryEntity implements Serializable {

	private static final long serialVersionUID = -1798070786993154676L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Id
	@Column(name = "NAME", unique = true, nullable = false, length = 100)
	private String name;

	@Column(name = "income", unique = false, nullable = false, columnDefinition = "tinyint(1) default 0")
	private boolean income;

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

	public Boolean getIncome() {
		return income;
	}

	public void setIncome(Boolean income) {
		this.income = income;
	}

	/**
	 * Hibernate requires no-args constructor
	 */
	public CategoryEntity() {
	}

	public CategoryEntity(String name) {
		this(name, false);
	}

	public CategoryEntity(String name, boolean income) {
		this.name = name.toLowerCase();
		this.income = income;
	}

	public CategoryEntity(CategoryDto category) {
		this.name = category.getName().toLowerCase();
	}

	@OneToMany(mappedBy = "category")
	private Set<TransactionEntity> transactions;

	public static boolean save(String name) {
		return CategoryEntity.save(name, false);
	}

	public static boolean save(String name, boolean income) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		String stId = null;
		try {
			tx = session.beginTransaction();
			CategoryEntity category = new CategoryEntity(name, income);
			stId = (String) session.save(category);
			tx.commit();
			Categories.addCategory(new CategoryDto(name));
			LOGGER.log(Level.INFO, "Création de la catégorie : {0}", name);
		} catch (HibernateException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
			if (tx != null)
				tx.rollback();
		} finally {
			session.close();
		}

		return stId != null;
	}

}
