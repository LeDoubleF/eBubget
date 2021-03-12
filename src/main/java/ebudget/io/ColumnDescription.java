package ebudget.io;

import java.util.Date;

public class ColumnDescription {

	private String name;
	private CVSParameter type;

	public ColumnDescription(CVSParameter type, String name) {
		this.type = type;
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public <T> T convert(String value) {

		switch (type) {
			case DOUBLE :
				return (T) new Double(value.replace(",", "."));

			case BOOLEAN :
				return (T) new Boolean(value);
			case DATE :
				return (T) new Date(value);

			default :
				return (T) value.trim();
		}
	}

	public String getName() {
		return name;
	}

	public CVSParameter getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ColumnDescription other = (ColumnDescription) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return (type == other.type);
	}

}
