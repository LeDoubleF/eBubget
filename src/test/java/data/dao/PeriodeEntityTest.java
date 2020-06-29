package data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import data.dto.PeriodDTo;

public class PeriodeEntityTest {
	static Logger logger = Logger.getLogger("CategoryEntityTest");

	@Test
	public void testEqualsPeriode() {
		PeriodeEntity periode1 = new PeriodeEntity(2020, 1, 3);
		PeriodeEntity periode2 = new PeriodeEntity(2020, 1, 3);
		PeriodeEntity periode3 = null;
		PeriodeEntity periode4 = new PeriodeEntity(2020, 1, 1);

		assertEquals(periode1, periode2);
		assertEquals(periode1, periode1);
		assertEquals(null, periode3);
		assertNotEquals(periode1, periode3);
		assertNotEquals(periode1, periode4);

	}

	@Test
	public void TestPeriodeEntitySave() {
		Transaction tx = null;
		try {
			// given
			Session session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			tx = session.beginTransaction();
			PeriodDTo periode = new PeriodDTo(2020, 1, 2);

			// when
			PeriodDTo periode1 = new PeriodDTo(2020, 1, 2);
			PeriodeEntity.save(periode);
			session.save(periode);
			tx.commit();
			// then
			Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
			tx = sessionTwo.beginTransaction();
			Query query = sessionTwo.createSQLQuery("SELECT * FROM PERIODE");
			query.executeUpdate();
			List idLlist = query.list();
			System.out.println(query.list() + " id " + idLlist.get(0));

			PeriodeEntity PeriodeEntity = (PeriodeEntity) session.load(PeriodeEntity.class,
					(Serializable) idLlist.get(0));
			System.out.println(PeriodeEntity);
			//
			// assertEquals("10/01/2020", transaction.getDate());
			// assertEquals("alimentation", transaction.getCategory().getName());
			// assertEquals("farine", transaction.getDescription());
			// assertEquals("Espèce", transaction.getPayment());
			// assertEquals(0.69, transaction.getAmount(), 0);
			// assertEquals(periode, transaction.getPeriode());

		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
		}
	}
}