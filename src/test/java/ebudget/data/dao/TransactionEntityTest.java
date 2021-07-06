package ebudget.data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.data.Categories;
import ebudget.data.Common;
import ebudget.data.dto.PaymentType;
import ebudget.data.dto.PeriodDTo;
import ebudget.data.dto.TransactionDto;

class TransactionEntityTest {

	private final double delta = 0.0;
	private final LocalDate date = LocalDate.of(2020, Month.JANUARY, 10);

	@BeforeEach
	public void clean() {
		Common.reinitializeDataBase();
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
			TransactionDto transactionDto = new TransactionDto(date, "alimentation", "farine", PaymentType.ESPECE, 0.69, periode);
			TransactionEntity.save(transactionDto);

			// then
			Session session = HibernateUtil.getSessionFactory()
				.openSession();
			tx = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT max(id) FROM TRANSACTION");
			query.executeUpdate();
			@SuppressWarnings({"unchecked"})
			List<Integer> idLlist = query.list();
			System.out.println(query.list() + " id " + idLlist.get(0));

			TransactionEntity transaction = (TransactionEntity) session.load(TransactionEntity.class, (Serializable) idLlist.get(0));

			assertEquals(date, transaction.getDate());
			assertEquals("alimentation", transaction.getCategory()
				.getName());
			assertEquals("farine", transaction.getDescription());
			assertEquals(PaymentType.ESPECE.toString(), transaction.getPayment());
			assertEquals(0.69, transaction.getAmount(), delta);
			assertEquals(2020, transaction.getPeriode()
				.getId()
				.getAnnee());
			assertEquals(1, transaction.getPeriode()
				.getId()
				.getTrimestre());
			assertEquals(2, transaction.getPeriode()
				.getId()
				.getMois());

		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
			e.printStackTrace();
			fail("transaction should be save");
		}
	}

	@Test
	void TestTransactionEntitySaveWhenCategoryNotExist() {
		Transaction tx = null;

		// given

		PeriodDTo periode = new PeriodDTo(2020, 2);
		PeriodEntity.save(periode);

		// when
		TransactionDto transactionDto = new TransactionDto(date, "alimentation", "farine", PaymentType.ESPECE, 0.69, periode);
		TransactionEntity.save(transactionDto);

		// then
		Session session = HibernateUtil.getSessionFactory()
			.openSession();
		tx = session.beginTransaction();
		Query query = session.createSQLQuery("SELECT max(id) FROM TRANSACTION");
		query.executeUpdate();
		@SuppressWarnings({"unchecked"})
		List<Integer> idLlist = query.list();
		System.out.println(query.list() + " id " + idLlist.get(0));

		TransactionEntity transaction = (TransactionEntity) session.load(TransactionEntity.class, (Serializable) idLlist.get(0));

		assertEquals(date, transaction.getDate());
		assertEquals(Categories.getDefaultCategory()
			.getName(),
				transaction.getCategory()
					.getName());
		assertEquals("farine", transaction.getDescription());
		assertEquals(PaymentType.ESPECE.toString(), transaction.getPayment());
		assertEquals(0.69, transaction.getAmount(), delta);
		assertEquals(2020, transaction.getPeriode()
			.getId()
			.getAnnee());
		assertEquals(1, transaction.getPeriode()
			.getId()
			.getTrimestre());
		assertEquals(2, transaction.getPeriode()
			.getId()
			.getMois());
	}

	@Test
	void TestTransactionEntitySum() {

		try {
			String categoryName = "test1";
			CategoryEntity.save(categoryName);

			PeriodDTo periode = new PeriodDTo(2020, 1);
			PeriodEntity.save(periode);

			TransactionDto transaction = new TransactionDto(date, categoryName, "description", PaymentType.RETRAIT, 20.0, periode);
			TransactionEntity.save(transaction);

			TransactionDto transaction2 = new TransactionDto(date, categoryName, "description", PaymentType.VIREMENT, 5.0, periode);
			TransactionEntity.save(transaction2);

			TransactionDto transaction3 = new TransactionDto(date, categoryName, "description", PaymentType.CB, 7.0, periode);
			TransactionEntity.save(transaction3);

			TransactionDto transaction4 = new TransactionDto(date, categoryName, "description", PaymentType.ESPECE, 17.0, periode);
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

		TransactionDto transaction = new TransactionDto(date, categoryName, "description", PaymentType.RETRAIT, 2.0, periode);
		TransactionEntity.save(transaction);

		TransactionDto transaction2 = new TransactionDto(date, categoryName, "description", PaymentType.ESPECE, 15.0, periode);
		TransactionEntity.save(transaction2);

		TransactionDto transaction3 = new TransactionDto(date, categoryName, "description", PaymentType.VIREMENT, 5.0, periode);
		TransactionEntity.save(transaction3);

		// when
		Double sum = TransactionEntity.sumCash();

		// then
		assertEquals(13.0, sum, delta);

	}

}
