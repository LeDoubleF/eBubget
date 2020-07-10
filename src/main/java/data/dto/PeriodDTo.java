package data.dto;

import exception.ComplianceException;
import exception.Message;

public class PeriodDTo {

	private int year;
	private int quarter;
	private int month;

	/**
	 * 
	 * @param year
	 * @param month
	 * @throws ComplianceException
	 *             if you put month>12||month<1
	 */
	public PeriodDTo(int year, int month) {
		super();
		this.year = year;
		this.quarter = calculateQuater(month);
		this.month = month;
	}

	private int calculateQuater(int month) {

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
		result = prime * result + year;
		result = prime * result + month;
		result = prime * result + quarter;
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
		if (year != other.year)
			return false;
		if (month != other.month)
			return false;
		if (quarter != other.quarter)
			return false;
		return true;
	}

	public int getYear() {
		return year;
	}

	public int getQuarter() {
		return quarter;
	}

	public int getMonth() {
		return month;
	}

}