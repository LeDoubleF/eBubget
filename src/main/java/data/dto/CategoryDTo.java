package data.dto;

public class CategoryDTo {

	private String name;

	public CategoryDTo() {
	}

	public CategoryDTo(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object o) {
		if (!(o instanceof CategoryDTo))
			return false;

		CategoryDTo category = (CategoryDTo) o;

		return name.equals(category.name);

	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + name.hashCode();

		return hash;
	}

}