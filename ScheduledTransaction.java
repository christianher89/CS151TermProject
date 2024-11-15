package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ScheduledTransaction {
	private String name;
	private String account;
	private String transactionType;
	private String freq = "Monthly"; //Hard coded as per problem statement
	private int dueDate;
	private double payAmount;
	
	private static ArrayList<ScheduledTransaction> schedTransactionList = new ArrayList<>();
	
	public ScheduledTransaction(String name, String acc, String transactionType, 
								String freq, int dueDate, double pay) {
		this.name = name;
		this.account = acc;
		this.transactionType = transactionType;
		//Skip freq as it is hard coded
		this.dueDate = dueDate;
		this.payAmount = pay;
	}
	
	//Getters for each field
	public String getName() {return name;}
	public String getAccount() {return account;}
	public String getTransactionType() {return transactionType;}
	public String getFrequency() {return freq;}
	public int getDueDate() {return dueDate;}
	public double getPayAmount() {return payAmount;}
	
	public static void storeScheduledTransaction(ScheduledTransaction newScheduled) throws IOException {
		schedTransactionList.add(newScheduled);
		
		File schedTransFile = new File("ScheduledTransactions.csv");
		try(PrintWriter out = new PrintWriter(new FileWriter(schedTransFile, true))){
			out.println(newScheduled.getName() + "," +
						newScheduled.getAccount() + "," +
						newScheduled.getTransactionType() + "," +
						newScheduled.getFrequency() + "," +
						newScheduled.getDueDate() + "," +
						newScheduled.getPayAmount());
		}
	}
}
