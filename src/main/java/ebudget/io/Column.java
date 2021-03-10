package ebudget.io;

import java.util.Date;

public class Column {

	private String name;
	private CVSParameter type;

	public Column(CVSParameter type, String name) {
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

}
