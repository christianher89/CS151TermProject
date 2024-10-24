package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Account {
	private String accName = "";
	private double startBal = 0;
	private String openDate = "";
	
	public Account (String name, double bal, String date) {
		accName = name;
		startBal = bal;
		openDate = date;
	}
	
	public String getName() {
		return accName;
	}

	public static void storeData(Account newAcc) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File accounts = new File("Accounts.csv");
		PrintWriter out = new PrintWriter(accounts);
		
		out.print(newAcc);
		out.close();
		
	}
}
