package ebudget.data.dao;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ebudget.data.dto.PeriodDTo;

@Entity
@Table(name = "Periode")
public class PeriodEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@EmbeddedId
	private PeriodePK id;

	public PeriodEntity(int annee, int trimestre, int mois) {
		super();
		this.id = new PeriodePK(annee, trimestre, mois);
	}

	public PeriodEntity() {
		super();
		// pour hibernate
	}

	public PeriodePK getId() {
		return id;
	}

	public void setId(PeriodePK id) {
		this.id = id;
	}

	public static boolean save(PeriodDTo periode) {
		return save(periode.getYear(), periode.getQuarter(), periode.getMonth());

	}

	public static boolean save(int annee, int trimestre, int mois) {
		Session session = HibernateUtil.getSessionFactory()
			.openSession();

		Transaction tx = null;
		PeriodePK stId = null;
		try {
			tx = session.beginTransaction();
			PeriodEntity periode = new PeriodEntity(annee, trimestre, mois);
			stId = (PeriodePK) session.save(periode);
			tx.commit();
		} catch (HibernateException ex) {
			LOGGER.log(Level.SEVERE, "Period={0}-{1}-{2} :{3}", new Object[]{annee, trimestre, mois, ex.getMessage()});
			if (tx != null)
				tx.rollback();
		} finally {
			session.close();
		}

		return stId != null;
	}

	/**
	 * supprime toutes les périodes
	 */
	public static void deleteAll() {
		Transaction tx = null;
		try {

			Session sessionTwo = HibernateUtil.getSessionFactory()
				.openSession();
			tx = sessionTwo.beginTransaction();

			Query queryDelete = sessionTwo.createSQLQuery("DELETE FROM periode");
			queryDelete.executeUpdate();

			sessionTwo.getTransaction()
				.commit();
			LOGGER.log(Level.INFO, "suppression de toutes les périodes");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "erreur lors de la suppression des périodes", e);
			// Rollback in case of an error occurred.
			if (tx != null)
				tx.rollback();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodEntity other = (PeriodEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
