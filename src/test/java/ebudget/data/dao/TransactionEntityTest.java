package ebudget.data.dao;

import static org.junit.Assert.assertEquals;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.data.Common;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;

class TransactionEntityTest {

	double delta = 0.0;

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

			// given

			CategoryEntity.save("alimentation");
			PeriodDTo periode = new PeriodDTo(2020, 2);
			PeriodEntity.save(periode);

			// when
			TransactionDto transactionDto = new TransactionDto("10/01/2020", "alimentation", "farine", "Espèce", 0.69, periode);
			TransactionEntity.save(transactionDto);

			// then
			Session session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT max(id) FROM TRANSACTION");
			query.executeUpdate();
			@SuppressWarnings({"unchecked"})
			List<Integer> idLlist = query.list();
			System.out.println(query.list() + " id " + idLlist.get(0));

			TransactionEntity transaction = (TransactionEntity) session.load(TransactionEntity.class, (Serializable) idLlist.get(0));

			assertEquals("10/01/2020", transaction.getDate());
			assertEquals("alimentation", transaction.getCategory().getName());
			assertEquals("farine", transaction.getDescription());
			assertEquals("Espèce", transaction.getPayment());
			assertEquals(0.69, transaction.getAmount(), delta);
			assertEquals(2020, transaction.getPeriode().getId().getAnnee());
			assertEquals(1, transaction.getPeriode().getId().getTrimestre());
			assertEquals(2, transaction.getPeriode().getId().getMois());

		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
			e.printStackTrace();
		}
	}

	@Test
	void TestTransactionEntitySum() {

		try {
			String categoryName = "test1";
			CategoryEntity.save(categoryName);

			PeriodDTo periode = new PeriodDTo(2020, 1);
			PeriodEntity.save(periode);

			TransactionDto transaction = new TransactionDto("01/01/2019", categoryName, "description", "retrait", 20.0, periode);
			TransactionEntity.save(transaction);

			TransactionDto transaction2 = new TransactionDto("01/01/2019", categoryName, "description", "virement", 5.0, periode);
			TransactionEntity.save(transaction2);

			TransactionDto transaction3 = new TransactionDto("01/01/2019", categoryName, "description", "carte", 7.0, periode);
			TransactionEntity.save(transaction3);

			TransactionDto transaction4 = new TransactionDto("01/01/2019", categoryName, "description", "Espèce", 17.0, periode);
			TransactionEntity.save(transaction4);
			// when
			Double sum = TransactionEntity.sumAccount();

			// then
			assertEquals(32.0, sum, delta);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void TestTransactionSumCash() {
		// given

		String categoryName = "test1";
		CategoryEntity.save(categoryName);

		PeriodDTo periode = new PeriodDTo(2020, 1);
		PeriodEntity.save(periode);

		TransactionDto transaction = new TransactionDto("01/01/2019", categoryName, "description", "retrait", 2.0, periode);
		TransactionEntity.save(transaction);

		TransactionDto transaction2 = new TransactionDto("01/01/2019", categoryName, "description", "Espèce", 15.0, periode);
		TransactionEntity.save(transaction2);

		TransactionDto transaction3 = new TransactionDto("01/01/2019", categoryName, "description", "virement", 5.0, periode);
		TransactionEntity.save(transaction3);

		// when
		Double sum = TransactionEntity.sumCash();

		// then
		assertEquals(13.0, sum, delta);

	}

}
