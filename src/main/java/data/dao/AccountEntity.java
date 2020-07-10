package data.dao;

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
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.dto.AccountDto;
import data.dto.AccountType;

@Entity
@Table(name = "Account", uniqueConstraints = { @UniqueConstraint(columnNames = "NAME") })
public class AccountEntity implements Serializable {

	private static final long serialVersionUID = -1798070786993154676L;
	static Logger logger = Logger.getLogger("AccountEntity");

	@Id
	@Column(name = "NAME", unique = true, nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	AccountType accountType;

	@Column(name = "Description", unique = false, nullable = false, length = 100)
	private String description;

	@Column(name = "initialAmount", unique = false, nullable = false)
	private Double initialAmount;

	@Column(name = "finalAmount", unique = false, nullable = false)
	private Double finalAmount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		String stId = null;
		try {
			tx = session.beginTransaction();
			AccountEntity accountEntity = new AccountEntity();
			accountEntity.setName(account.getName());
			accountEntity.setAccountType(account.getAccountType());
			accountEntity.setDescription(account.getDescription());
			accountEntity.setInitialAmount(account.getInitialAmount());
			accountEntity.setFinalAmount(account.getFinalAmount());

			stId = (String) session.save(accountEntity);
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

}