package data.dao;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import data.dto.PeriodDTo;

@Entity
@Table(name = "Periode")
public class PeriodeEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger("PeriodeEntity");
	@EmbeddedId
	private PeriodePK id;

	public PeriodeEntity(int annee, int trimestre, int mois) {
		super();
		this.id = new PeriodePK(annee, trimestre, mois);
	}

	public PeriodeEntity() {
		super();
		// pour hibernate
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
		PeriodeEntity other = (PeriodeEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public PeriodePK getId() {
		return id;
	}

	public void setId(PeriodePK id) {
		this.id = id;
	}

	public static boolean save(PeriodDTo periode) {
		return save(periode.getAnnee(), periode.getTrimestre(), periode.getMois());

	}

	public static boolean save(int annee, int trimestre, int mois) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;
		PeriodePK stId = null;
		try {
			tx = session.beginTransaction();
			PeriodeEntity periode = new PeriodeEntity(annee, trimestre, mois);
			stId = (PeriodePK) session.save(periode);
			tx.commit();
		} catch (HibernateException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			if (tx != null)
				tx.rollback();
		} finally {
			session.close();
		}

		return stId != null;
	}
}
