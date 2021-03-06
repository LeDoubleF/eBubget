package ebudget.data.dao;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.AccountType;

@Entity
@Table(name = "Account", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME")})
public class AccountEntity implements Serializable {

	private static final long serialVersionUID = -1798070786993154676L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Id
	@Column(name = "NAME", unique = true, nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	AccountType accountType;

	@Column(name = "main", unique = false, nullable = false, columnDefinition = "tinyint(1) default 0")
	private boolean main;

	@Column(name = "initialAmount", unique = false, nullable = false)
	private Double initialAmount;

	@Column(name = "finalAmount", unique = false, nullable = false)
	private Double finalAmount;

	public AccountEntity() {
		super();
	}

	public AccountEntity(AccountDto account) {
		super();
		this.name = account.getName();
		this.accountType = account.getAccountType();
		this.main = account.isMain();
		this.initialAmount = account.getInitialAmount();
		this.finalAmount = account.getFinalAmount();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	public boolean getMain() {
		return main;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public Double getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(Double initialAmount) {
		this.initialAmount = initialAmount;
	}

	public Double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public static boolean save(AccountDto account) {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();

		Transaction tx = null;
		String stId = null;
		try {
			tx = session.beginTransaction();
			AccountEntity accountEntity = new AccountEntity();
			accountEntity.setName(account.getName());
			accountEntity.setAccountType(account.getAccountType());
			accountEntity.setInitialAmount(account.getInitialAmount());
			accountEntity.setFinalAmount(account.getFinalAmount());
			accountEntity.setMain(account.isMain());

			stId = (String) session.save(accountEntity);
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
	 * supprime toutes les p�riodes
	 */
	public static void deleteAll() {
		Transaction tx = null;
		try {

			Session sessionTwo = HibernateUtil.getSessionFactory()
				.openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("DELETE FROM Account");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction()
				.commit();
			LOGGER.log(Level.INFO, "suppression de toutes les comptes");

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "erreur lors de la suppression des comptes", e);
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}
	}

}