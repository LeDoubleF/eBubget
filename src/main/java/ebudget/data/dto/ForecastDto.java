package ebudget.data.dto;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ForecastDto {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private CategoryDto category;
	private String description;
	private Double amount;
	private Boolean mandatory = false;
	private Boolean variable = false;
	private Boolean income = false;
	private Boolean january = false;
	private Boolean february = false;
	private Boolean march = false;
	private Boolean april = false;
	private Boolean may = false;
	private Boolean june = false;
	private Boolean july = false;
	private Boolean august = false;
	private Boolean september = false;
	private Boolean october = false;
	private Boolean november = false;
	private Boolean december = false;

	public ForecastDto(String category, String description, Double amount, Boolean mandatory, Boolean variable, Boolean income, Boolean january,
			Boolean february, Boolean march, Boolean april, Boolean may, Boolean june, Boolean july, Boolean august, Boolean september,
			Boolean october, Boolean november, Boolean december) {
		super();
		this.category = new CategoryDto(category);
		this.description = description;
		this.amount = amount;
		this.mandatory = mandatory;
		this.variable = variable;
		this.income = income;
		this.january = january;
		this.february = february;
		this.march = march;
		this.april = april;
		this.may = may;
		this.june = june;
		this.july = july;
		this.august = august;
		this.september = september;
		this.october = october;
		this.november = november;
		this.december = december;
		LOGGER.log(Level.INFO, "Création forecastDto");
	}

	public CategoryDto getCategory() {
		return category;
	}

	public void setCategory(CategoryDto category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean getVariable() {
		return variable;
	}

	public void setVariable(Boolean variable) {
		this.variable = variable;
	}

	public Boolean getIncome() {
		return income;
	}

	public void setIncome(Boolean income) {
		this.income = income;
	}

	public Boolean getJanuary() {
		return january;
	}

	public void setJanuary(Boolean january) {
		this.january = january;
	}

	public Boolean getFebruary() {
		return february;
	}

	public void setFebruary(Boolean february) {
		this.february = february;
	}

	public Boolean getMarch() {
		return march;
	}

	public void setMarch(Boolean march) {
		this.march = march;
	}

	public Boolean getApril() {
		return april;
	}

	public void setApril(Boolean april) {
		this.april = april;
	}

	public Boolean getMay() {
		return may;
	}

	public void setMay(Boolean may) {
		this.may = may;
	}

	public Boolean getJune() {
		return june;
	}

	public void setJune(Boolean june) {
		this.june = june;
	}

	public Boolean getJuly() {
		return july;
	}

	public void setJuly(Boolean july) {
		this.july = july;
	}

	public Boolean getAugust() {
		return august;
	}

	public void setAugust(Boolean august) {
		this.august = august;
	}

	public Boolean getSeptember() {
		return september;
	}

	public void setSeptember(Boolean september) {
		this.september = september;
	}

	public Boolean getOctober() {
		return october;
	}

	public void setOctober(Boolean october) {
		this.october = october;
	}

	public Boolean getNovember() {
		return november;
	}

	public void setNovember(Boolean november) {
		this.november = november;
	}

	public Boolean getDecember() {
		return december;
	}

	public void setDecember(Boolean december) {
		this.december = december;
	}

}
