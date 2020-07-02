package data.dao;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import data.DataTest;
import data.dto.PeriodDTo;
import data.dto.TransactionDto;

public class TransactionEntityTest {

	@Test
	public void TestTransactionEntitySave() {
		Transaction tx = null;
		try {
			// given
			Session session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			CategoryEntity category = new CategoryEntity("alimentation");
			session.save(category);
			tx.commit();

			// when
			PeriodDTo periode = new PeriodDTo(2020, 1, 1);
			TransactionDto transactionDto = new TransactionDto("10/01/2020", "alimentation", "farine", "Espèce", 0.69,
					periode);
			TransactionEntity.save(transactionDto);

			// then
			Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
			tx = sessionTwo.beginTransaction();
			Query query = sessionTwo.createSQLQuery("SELECT max(id) FROM TRANSACTION");
			query.executeUpdate();
			List idLlist = query.list();
			System.out.println(query.list() + " id " + idLlist.get(0));

			TransactionEntity transaction = (TransactionEntity) session.load(TransactionEntity.class,
					(Serializable) idLlist.get(0));
			System.out.println(transaction.getCategory().getName() + " " + transaction.getDate() + " "
					+ transaction.getDescription());
			assertEquals("10/01/2020", transaction.getDate());
			assertEquals("alimentation", transaction.getCategory().getName());
			assertEquals("farine", transaction.getDescription());
			assertEquals("Espèce", transaction.getPayment());
			assertEquals(0.69, transaction.getAmount(), 0);
			assertEquals(periode, transaction.getPeriode());

			DataTest.deleteTransaction();
			DataTest.deleteCategory();
		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
		}
	}

	@Test
	public void TestTransactionEntitySum() {
		DataTest.deleteTransaction();
		DataTest.deleteCategory();

		// given
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		Integer stId = null;
		tx = session.beginTransaction();
		CategoryEntity category = new CategoryEntity("test1");
		session.save(category);

		PeriodeEntity.save(2020, 1, 1);

		TransactionEntity transaction = new TransactionEntity();
		transaction.setPeriode(2020, 1, 1);
		transaction.setDate("01/01/2019");
		transaction.setCategory(category);
		transaction.setDescription("description");
		transaction.setPayment("payement");
		transaction.setAmount(2.00);
		session.save(transaction);

		TransactionEntity transaction2 = new TransactionEntity();
		transaction2.setPeriode(2020, 1, 1);
		transaction2.setDate("01/01/2019");
		transaction2.setCategory(category);
		transaction2.setDescription("description");
		transaction2.setPayment("payement");
		transaction2.setAmount(5.00);
		session.save(transaction2);
		tx.commit();
		// when
		Double sum = TransactionEntity.sum();

		// then
		assertEquals(sum, 7.0, 0);

		DataTest.deleteTransaction();
		DataTest.deleteCategory();
		DataTest.deletePeriode();

	}

}
