package data.dto;

public class PeriodDTo {

	private int annee;
	private int trimestre;
	private int mois;

	public PeriodDTo(int annee, int trimestre, int mois) {
		super();
		this.annee = annee;
		this.trimestre = trimestre;
		this.mois = mois;
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

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(int trimestre) {
		this.trimestre = trimestre;
	}

	public int getMois() {
		return mois;
	}

	public void setMois(int mois) {
		this.mois = mois;
	}

}