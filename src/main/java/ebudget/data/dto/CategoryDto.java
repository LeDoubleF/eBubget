package ebudget.data.dto;

public class CategoryDto implements Comparable<CategoryDto> {

	private String name;
	private Boolean income = false;

	public CategoryDto() {
	}

	public CategoryDto(String name) {
		this(name, false);
	}

	public Boolean isIncome() {
		return income;
	}

	public void setIncome(Boolean income) {
		this.income = income;
	}

	public CategoryDto(String name, boolean income) {
		super();
		this.name = name.toLowerCase();
		this.income = income;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(CategoryDto other) {
		if (this.equals(other))
			return 0;
		else if (name.equals(other.getName()))
			return income.compareTo(other.isIncome());
		else
			return name.compareToIgnoreCase(other.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((income == null) ? 0 : income.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CategoryDto other = (CategoryDto) obj;
		if (income == null) {
			if (other.income != null)
				return false;
		} else if (!income.equals(other.income))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}