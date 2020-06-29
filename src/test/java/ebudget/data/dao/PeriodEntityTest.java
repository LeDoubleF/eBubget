package ebudget.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ebudget.data.Repository;
import ebudget.data.dto.PeriodDTo;

class PeriodEntityTest {

	static Logger logger = Logger.getLogger("PeriodeEntityTest");

	@BeforeEach
	public void clean() {
		PeriodEntity.deleteAll();
	}

	@AfterAll
	public static void cleanUp() {
		Repository.clearDataBase();
	}

	@Test
	void testEqualsPeriode() {

		PeriodEntity periode1 = new PeriodEntity(2020, 1, 3);
		PeriodEntity periode2 = new PeriodEntity(2020, 1, 3);
		PeriodEntity periode3 = null;
		PeriodEntity periode4 = new PeriodEntity(2020, 1, 1);

		assertEquals(periode1, periode2);
		assertEquals(periode1, periode1);
		assertEquals(null, periode3);
		assertNotEquals(periode1, periode3);
		assertNotEquals(periode1, periode4);
		assertNotSame(periode1, periode2);
	}

	@Test
	void TestPeriodeEntitySave() {

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

			PeriodEntity periodEntity = (PeriodEntity) session.load(PeriodEntity.class, (Serializable) new PeriodePK(2020, 4, 11));

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