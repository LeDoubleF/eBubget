package data.dao;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PeriodePK implements Serializable {
	static Logger logger = Logger.getLogger("PeriodePK");
	private static final long serialVersionUID = 1L;

	@Column(name = "annee")
	private int annee;

	@Column(name = "trimestre")
	private int trimestre;

	@Column(name = "mois")
	private int mois;

	public PeriodePK(int annee, int trimestre, int mois) {
		super();
		this.annee = annee;
		this.trimestre = trimestre;
		this.mois = mois;
	}

	public PeriodePK() {
		super();
		// pour hibernate
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(int trimestre) {
		this.trimestre = trimestre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + annee;
		result = prime * result + mois;
		result = prime * result + trimestre;
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
		PeriodePK other = (PeriodePK) obj;
		if (annee != other.annee)
			return false;
		if (mois != other.mois)
			return false;
		if (trimestre != other.trimestre)
			return false;
		return true;
	}

	public int getMois() {
		return mois;
	}

	public void setMois(int mois) {
		this.mois = mois;
	}

}
