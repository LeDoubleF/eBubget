package ebudget.io;

public class ColumnDescription {

	private int columnNum;
	private String type;
	private String name;

	public ColumnDescription(int columnNum, String type, String name) {
		this.columnNum = columnNum;
		this.type = type;
		this.name = name;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
