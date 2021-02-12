package ebudget.data.dao;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ebudget.data.Categories;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.ForecastDto;

@Entity
@Table(name = "RecurringExpenses", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class RecurringExpensesEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 932033603326601211L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer recurringExpensesID;

	@ManyToOne
	@JoinColumn(name = "Category", nullable = false)
	private CategoryEntity category;

	@Column(name = "Description", unique = false, nullable = false, length = 100)
	private String description;

	@Column(name = "amount", unique = false, nullable = false)
	private Double amount;

	@Column(name = "mandatory", unique = false, nullable = false)
	private boolean mandatory = false;

	@Column(name = "income", unique = false, nullable = false)
	private boolean income = false;

	@Column(name = "variable", unique = false, nullable = false)
	private boolean variable = false;

	@Column(name = "january", unique = false, nullable = false)
	private boolean january = false;

	@Column(name = "february", unique = false, nullable = false)
	private boolean february = false;

	@Column(name = "march", unique = false, nullable = false)
	private boolean march = false;

	@Column(name = "april", unique = false, nullable = false)
	private boolean april = false;

	@Column(name = "may", unique = false, nullable = false)
	private boolean may = false;

	@Column(name = "june", unique = false, nullable = false)
	private boolean june = false;

	@Column(name = "july", unique = false, nullable = false)
	private boolean july = false;

	@Column(name = "august", unique = false, nullable = false)
	private boolean august = false;

	@Column(name = "september", unique = false, nullable = false)
	private boolean september = false;

	@Column(name = "october", unique = false, nullable = false)
	private boolean october = false;

	@Column(name = "november", unique = false, nullable = false)
	private boolean november = false;

	@Column(name = "december", unique = false, nullable = false)
	private boolean december = false;

	/**
	 * supprime toutes les cat�gories et rajoute divers
	 */
	public static void deleteAll() {
		Transaction tx = null;
		try {

			Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("DELETE FROM forecast");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction().commit();
			LOGGER.log(Level.INFO, "suppression de toutes les pr�visions");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "erreur lors de la suppression des pr�visions", e);
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}
	}

	public static boolean save(ForecastDto forecast) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		Integer stId = null;
		try {
			CategoryDto categoryDivers = new CategoryDto("divers");
			tx = session.beginTransaction();

			// TODO mutualiser la recherche des category
			if (!Categories.isCategory(forecast.getCategory())) {
				LOGGER.log(Level.WARNING, "{0} n''existe pas en tant que cat�gorie ", forecast.getCategory().getName());
				forecast.setCategory(categoryDivers);
			}

			RecurringExpensesEntity forecastEntity = new RecurringExpensesEntity(forecast);
			stId = (Integer) session.save(forecastEntity);
			tx.commit();
		} catch (HibernateException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
			if (tx != null)
				tx.rollback();
		} finally {
			session.close();
		}

		return stId != null;
	}

	/**
	 * Hibernate requires no-args constructor
	 */
	public RecurringExpensesEntity() {

	}

	public RecurringExpensesEntity(ForecastDto forecast) {
		this.category = new CategoryEntity(forecast.getCategory());
		this.description = forecast.getDescription();
		this.amount = forecast.getAmount();
		this.mandatory = forecast.getMandatory();
		this.income = forecast.getIncome();
		this.variable = forecast.getVariable();
		this.january = forecast.getJanuary();
		this.february = forecast.getFebruary();
		this.march = forecast.getMarch();
		this.april = forecast.getApril();
		this.may = forecast.getMay();
		this.june = forecast.getJune();
		this.july = forecast.getJuly();
		this.august = forecast.getAugust();
		this.september = forecast.getSeptember();
		this.october = forecast.getOctober();
		this.november = forecast.getNovember();
		this.december = forecast.getDecember();
	}
}