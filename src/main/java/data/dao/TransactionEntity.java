package data.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.Repository;
import data.dto.CategoryDTo;
import data.dto.PeriodDTo;
import data.dto.TransactionDto;

@Entity
@Table(name = "Transaction", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class TransactionEntity implements Serializable {

	private static final long serialVersionUID = -1798070786993154676L;
	static Logger logger = Logger.getLogger("TransactionEntity");
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer transactionId;

	@Column(name = "Date", unique = false, nullable = false)
	private String date;

	@ManyToOne
	@JoinColumn(name = "Category", nullable = false)
	private CategoryEntity category;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "annee", nullable = false), @JoinColumn(name = "trimestre", nullable = false),
			@JoinColumn(name = "mois", nullable = false) })
	private PeriodEntity periode;

	public PeriodEntity getPeriode() {
		return periode;
	}

	public void setPeriode(int annee, int trimestre, int mois) {
		this.periode = new PeriodEntity(annee, trimestre, mois);
	}

	public void setPeriode(PeriodDTo periode) {
		this.periode = new PeriodEntity(periode.getYear(), periode.getQuarter(), periode.getMonth());
	}

	@Column(name = "Description", unique = false, nullable = false, length = 100)
	private String description;

	@Column(name = "Payment", unique = false, nullable = false, length = 100)
	private String payment;

	@Column(name = "amount", unique = false, nullable = false)
	private Double amount;

	public Integer getTransactionId() {
		return transactionId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryDTo category) {
		this.category = new CategoryEntity(category.getName());
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	// TODO tester quand la category n'existe pas
	public static boolean save(TransactionDto transaction) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		Integer stId = null;
		try {
			CategoryDTo categoryDivers = new CategoryDTo("divers");
			tx = session.beginTransaction();
			TransactionEntity transactionEntity = new TransactionEntity();
			transactionEntity.setDate(transaction.getDate());
			// TODO gestion correcte de la date
			if (Repository.isCategory(transaction.getCategory())) {
				transactionEntity.setCategory(transaction.getCategory());
				System.out.println("\t oui");
			} else {
				logger.log(Level.WARNING, transaction.getCategory().getName() + " n'existe pas en tant que cat�gorie ");
				transactionEntity.setCategory(categoryDivers);
			}
			transactionEntity.setDescription(transaction.getDescription());
			transactionEntity.setPayment(transaction.getPayment());
			transactionEntity.setAmount(transaction.getAmount());
			transactionEntity.setPeriode(transaction.getPeriode());
			stId = (Integer) session.save(transactionEntity);
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

	public static Double sumAccount() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String hql = "SELECT SUM(E.amount)FROM " + TransactionEntity.class.getName() + " E "
					+ " WHERE E.payment <>'esp�ce' ";
			Query query = session.createQuery(hql);

			// Execute query.
			@SuppressWarnings({ "unchecked" })
			List<Double> sum = query.list();

			// Commit data.
			session.getTransaction().commit();

			double total = BigDecimal.valueOf(sum.get(0)).setScale(2, RoundingMode.HALF_UP).doubleValue();
			System.out.println("Somme total: " + total);
			return total;
		} catch (Exception e) {
			// logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			// Rollback in case of an error occurred.
			tx.rollback();
		}
		return null;

	}

	public static void sumByCategory() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String hql = "SELECT E.category,E.periode, SUM(E.amount) FROM " + TransactionEntity.class.getName() + " E "
					+ "GROUP BY E.category,E.periode order by E.category";
			Query query = session.createQuery(hql);

			// Execute query.
			@SuppressWarnings({ "unchecked" })
			List<Object[]> sum = query.list();

			// Commit data.
			session.getTransaction().commit();

			for (Object[] a : sum)
				System.out.println(a[0] + " " + a[1] + " " + a[2]);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			tx.rollback();
		}
	}

	public static Double sumCash() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String sqlMore = "SELECT SUM(E.amount) FROM " + TransactionEntity.class.getName() + " E "
					+ " WHERE E.payment ='esp�ce' ";
			Query queryMore = session.createQuery(sqlMore);

			String sqlLess = "SELECT SUM(E.amount) FROM " + TransactionEntity.class.getName() + " E "
					+ " WHERE E.payment='retrait' ";
			Query queryLess = session.createQuery(sqlLess);

			// Execute query.
			@SuppressWarnings({ "unchecked" })
			List<Double> sumMore = queryMore.list();
			@SuppressWarnings({ "unchecked" })
			List<Double> sumLess = queryLess.list();

			return sumMore.get(0) - sumLess.get(0);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}