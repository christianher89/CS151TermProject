package application;

public class TransactionType {
	private String name = "";
	
	public TransactionType(String typeName) {
		name = typeName;
	}
	
	public String getName() {
		return name;
	}
}
