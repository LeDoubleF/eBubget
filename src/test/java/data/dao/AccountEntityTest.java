package data.dao;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import data.dto.AccountDto;
import data.dto.AccountType;

public class AccountEntityTest {

	@Test
	public void TestTransactionEntitySave() {
		Transaction tx = null;
		try {
			String description = "compte courant";
			AccountDto cpp = new AccountDto("pactole", AccountType.CPP, description, 0.0, 100.0);
			AccountEntity.save(cpp);

			String name = new String("pactole");
			Session session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			Query query = session.createSQLQuery("SELECT * FROM account WHERE NAME='" + name + "' ");
			query.executeUpdate();
			List idLlist = query.list();
			System.out.println(" id ");
			System.out.println(query.list() + " id " + idLlist.get(0));

			AccountEntity account = (AccountEntity) session.load(AccountEntity.class, (Serializable) "pactole");
			assertEquals(name, account.getName());
			assertEquals(AccountType.CPP, account.getAccountType());
			assertEquals(description, account.getDescription());
			assertEquals(0.0, account.getInitialAmount(), 0);
			assertEquals(100.0, account.getFinalAmount(), 0);

		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
			e.printStackTrace();
		}
	}

}
