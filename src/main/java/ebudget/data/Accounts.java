package ebudget.data;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ebudget.data.dao.AccountEntity;
import ebudget.data.dao.CategoryEntity;
import ebudget.data.dao.HibernateUtil;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.AccountType;
import ebudget.data.dto.CategoryDto;

/**
 * Permet de connaitre et de gérer les comptes existants
 * 
 * @author ffazer
 *
 */
public class Accounts {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static Map<String, AccountDto> allAccounts = new HashMap<String, AccountDto>();

	private static AccountDto defaultAccount = new AccountDto("cpp", AccountType.CPP, true, 0.0);

	Accounts() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Récupere la categorie par défaut dénit dans le fichier de properties
	 *
	 * @return catégorie par défaut
	 */
	public static AccountDto getDefaultAccount() {
		return defaultAccount;
	}

	public static List<AccountDto> getAllAccounts() {
		return (List<AccountDto>) allAccounts.values();
	}

	public static AccountDto getAccounts(String accountName) {
		return allAccounts.get(accountName);
	}

	public static void addAccount(AccountDto account) {
		AccountEntity.save(account);
		allAccounts.put(account.getName(), account);
	}

	public static void clearAllAccounts() {
		allAccounts.clear();
	}

	public static void initAccounts() {

		if (allAccounts.size() == 0) {
			if (countAccountEntity().equals(BigInteger.valueOf(0))) {
				addAccount(defaultAccount);

			} else {
				fillAllAccounts();
			}
		}
	}

	/**
	 * compter le nombre de compte en base
	 *
	 * @return
	 */
	protected static BigInteger countAccountEntity() {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT count(*) FROM ACCOUNT");
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

	protected static void fillAllAccounts() {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String hql = "FROM AccountEntity";
			Query query = session.createQuery(hql);
			@SuppressWarnings("unchecked")
			List<AccountEntity> listAccount = query.list();

			for (AccountEntity account : listAccount) {
				allAccounts.put(account.getName(),
						new AccountDto(account.getName(), account.getAccountType(), account.getMain(), account.getInitialAmount()));
				if (account.getMain()) {
					defaultAccount = getAccounts(account.getName());
				}
			}
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

}