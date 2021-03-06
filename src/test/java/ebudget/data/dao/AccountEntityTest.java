package ebudget.data.dao;

import static org.junit.Assert.assertEquals;
import java.io.Serializable;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.data.Common;
import ebudget.data.dto.AccountDto;
import ebudget.data.dto.AccountType;

class AccountEntityTest {

	@BeforeEach
	public void clean() {
		Common.clearDataBase();
	}

	@AfterAll
	public static void cleanUp() {
		Common.clearDataBase();
	}

	@Test
	void TestTransactionEntitySave() {
		Transaction tx = null;
		try {
			String description = "compte courant";
			double delta = 0.0;
			AccountDto cpp = new AccountDto("pactole", AccountType.CPP, false, delta);
			AccountEntity.save(cpp);

			String name = new String("pactole");
			Session session = HibernateUtil.getSessionFactory()
				.openSession();
			tx = session.beginTransaction();

			Query query = session.createSQLQuery("SELECT * FROM account WHERE NAME='" + name + "' ");
			query.executeUpdate();

			AccountEntity account = (AccountEntity) session.load(AccountEntity.class, (Serializable) "pactole");
			assertEquals(name, account.getName());
			assertEquals(AccountType.CPP, account.getAccountType());
			assertEquals(delta, account.getInitialAmount(), delta);
			assertEquals(0.0, account.getFinalAmount(), delta);

		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
			e.printStackTrace();
		}
	}

}
