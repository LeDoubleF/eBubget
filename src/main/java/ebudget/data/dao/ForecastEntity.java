package ebudget.data.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Forecast", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class ForecastEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer ForecastId;

	@ManyToOne
	@JoinColumn(name = "Category", nullable = false)
	private CategoryEntity category;

	@Column(name = "Description", unique = false, nullable = false, length = 100)
	private String description;

	@Column(name = "amount", unique = false, nullable = false)
	private Double amount;

	@Column(name = "mandatory", unique = false, nullable = false)
	private boolean mandatory = false;

	@Column(name = "income", unique = false, nullable = false)
	private boolean income = false;

	@Column(name = "variable", unique = false, nullable = false)
	private boolean variable = false;

	@Column(name = "january", unique = false, nullable = false)
	private boolean january = false;

	@Column(name = "february", unique = false, nullable = false)
	private boolean february = false;

	@Column(name = "march", unique = false, nullable = false)
	private boolean march = false;

	@Column(name = "april", unique = false, nullable = false)
	private boolean april = false;

	@Column(name = "may", unique = false, nullable = false)
	private boolean may = false;

	@Column(name = "june", unique = false, nullable = false)
	private boolean june = false;

	@Column(name = "july", unique = false, nullable = false)
	private boolean july = false;

	@Column(name = "august", unique = false, nullable = false)
	private boolean august = false;

	@Column(name = "september", unique = false, nullable = false)
	private boolean september = false;

	@Column(name = "october", unique = false, nullable = false)
	private boolean october = false;

	@Column(name = "november", unique = false, nullable = false)
	private boolean november = false;

	@Column(name = "december", unique = false, nullable = false)
	private boolean december = false;
}
