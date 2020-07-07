package data.dto;

import exception.ComplianceException;
import exception.Message;

public class PeriodDTo {

	private int annee;
	private int trimestre;
	private int mois;

	/**
	 * 
	 * @param annee
	 * @param mois
	 * @throws ComplianceException
	 *             if you put month>12||month<1
	 */
	public PeriodDTo(int annee, int mois) {
		super();
		this.annee = annee;
		this.trimestre = calculateTrimestre(mois);
		this.mois = mois;
	}

	private int calculateTrimestre(int month) {

		if (month >= 1 && month <= 3)
			return 1;
		else if (month >= 4 && month <= 6)
			return 2;
		else if (month >= 7 && month <= 9)
			return 3;
		else if (month >= 8 && month <= 12)
			return 4;
		else
			throw new IllegalArgumentException(Message.INVALD_MONTH);
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
		PeriodDTo other = (PeriodDTo) obj;
		if (annee != other.annee)
			return false;
		if (mois != other.mois)
			return false;
		if (trimestre != other.trimestre)
			return false;
		return true;
	}

	public int getAnnee() {
		return annee;
	}

	public int getTrimestre() {
		return trimestre;
	}

	public int getMois() {
		return mois;
	}

}