package ebudget.data.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
import ebudget.data.Accounts;
import ebudget.data.Categories;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.AccountType;
import ebudget.data.dto.CategoryDto;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;

@Entity
@Table(name = "Transaction", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class TransactionEntity implements Serializable {

	private static final long serialVersionUID = -1798070786993154676L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer transactionId;

	@ManyToOne
	@JoinColumn(name = "Category", nullable = false)
	private CategoryEntity category;

	@ManyToOne
	@JoinColumn(name = "Account", nullable = false)
	private AccountEntity account;

	@ManyToOne
	@JoinColumns({@JoinColumn(name = "annee", nullable = false), @JoinColumn(name = "trimestre", nullable = false),
			@JoinColumn(name = "mois", nullable = false)})
	private PeriodEntity periode;

	@Column(name = "Date", unique = false, nullable = false)
	private LocalDate date;

	@Column(name = "Description", unique = false, nullable = false, length = 100)
	private String description;

	@Column(name = "Payment", unique = false, nullable = false, length = 100)
	private String payment;

	@Column(name = "amount", unique = false, nullable = false)
	private Double amount;

	public PeriodEntity getPeriode() {
		return periode;
	}

	public void setPeriode(int annee, int trimestre, int mois) {
		this.periode = new PeriodEntity(annee, trimestre, mois);
	}

	public void setPeriode(PeriodDTo periode) {
		this.periode = new PeriodEntity(periode.getYear(), periode.getQuarter(), periode.getMonth());
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
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

	public void setCategory(CategoryDto category) {
		this.category = new CategoryEntity(category.getName());
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;

	}

	public void setAccount(AccountDto account) {
		this.account = new AccountEntity(account);

	}

	public static boolean save(TransactionDto transaction) {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();

		Transaction tx = null;
		Integer stId = null;
		try {
			System.out.println(transaction.getDescription());
			System.out.println(transaction.getCategory()
				.getName());
			tx = session.beginTransaction();
			TransactionEntity transactionEntity = new TransactionEntity();
			transactionEntity.setDate(transaction.getDate());
			if (Categories.isCategory(transaction.getCategory())) {
				transactionEntity.setCategory(transaction.getCategory());
				LOGGER.log(Level.INFO, "{0} existe ", transaction.getCategory()
					.getName());
			} else {
				LOGGER.log(Level.WARNING, "{0} n existe pas en tant que catégorie ", transaction.getCategory()
					.getName());

				transactionEntity.setCategory(Categories.getDefaultCategory());
			}
			transactionEntity.setDescription(transaction.getDescription());
			transactionEntity.setPayment(transaction.getPaymentString());
			transactionEntity.setAmount(transaction.getAmount());
			transactionEntity.setPeriode(transaction.getPeriod());
			transactionEntity.setAccount(transaction.getAccount());
			stId = (Integer) session.save(transactionEntity);
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

	public static Double sumCash() {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();

		try {
			session.beginTransaction();

			String sqlMore = "SELECT SUM(E.amount) FROM " + TransactionEntity.class.getName() + " E " + " WHERE E.payment ='espèce' ";
			Query queryMore = session.createQuery(sqlMore);

			String sqlLess = "SELECT SUM(E.amount) FROM " + TransactionEntity.class.getName() + " E " + " WHERE E.payment='retrait' ";
			Query queryLess = session.createQuery(sqlLess);

			// Execute query.
			@SuppressWarnings({"unchecked"})
			List<Double> sumMore = queryMore.list();
			@SuppressWarnings({"unchecked"})
			List<Double> sumLess = queryLess.list();

			return sumMore.get(0) - sumLess.get(0);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public static void deleteAll() {
		Transaction tx = null;
		try {

			Session sessionTwo = HibernateUtil.getSessionFactory()
				.openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("delete from  transaction");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction()
				.commit();
		} catch (Exception e) {
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}
	}

	public static Double sum(AccountEntity accountToSum) {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String hql = "SELECT SUM(T.amount) FROM " + TransactionEntity.class.getName() + " T where T.account= :accountToSum";
			Double sum = (Double) session.createQuery(hql)
				.setParameter("accountToSum", accountToSum)
				.getSingleResult();
			if (sum == null)
				return 0.0;
			return sum;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "erreur calcul somme total ", e);

			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}
		return null;

	}

	public static Double sumMainAccount() {
		return sum(Accounts.getDefaultAccount()
			.getName());
	}

	public static Double sum(String accountName) {
		AccountEntity account = AccountEntity.get(accountName);
		return sum(account);
	}
}