package data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import data.dto.PeriodDTo;

public class PeriodEntityTest {
	static Logger logger = Logger.getLogger("PeriodeEntityTest");

	@Test
	public void testEqualsPeriode() {
		PeriodEntity periode1 = new PeriodEntity(2020, 1, 3);
		PeriodEntity periode2 = new PeriodEntity(2020, 1, 3);
		PeriodEntity periode3 = null;
		PeriodEntity periode4 = new PeriodEntity(2020, 1, 1);

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
			PeriodDTo periode = new PeriodDTo(2020, 11);

			// when
			PeriodEntity.save(periode);
			// then
			Session session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT count(*) FROM PERIODE");
			query.executeUpdate();
			@SuppressWarnings("unchecked")
			List<BigInteger> nb = query.list();

			PeriodEntity periodEntity = (PeriodEntity) session.load(PeriodEntity.class,
					(Serializable) new PeriodePK(2020, 4, 11));

			assertEquals(2020, periodEntity.getId().getAnnee());
			assertEquals(4, periodEntity.getId().getTrimestre());
			assertEquals(11, periodEntity.getId().getMois());
			assertEquals(new BigInteger("1"), nb.get(0));

		} catch (Exception e) {
			// Rollback in case of an error occurred.
			tx.rollback();
			e.printStackTrace();
		}
	}
}