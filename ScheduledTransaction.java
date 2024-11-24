package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ScheduledTransaction implements Editable{
	private String name;
	private String account;
	private String transactionType;
	private String freq = "Monthly"; //Hard coded as per problem statement
	private int dueDate;
	private double payAmount;
	
	private static ArrayList<ScheduledTransaction> schedTransactionList = new ArrayList<>();
	
	public ScheduledTransaction(String name, String acc, String transactionType, 
								int dueDate, double pay) {
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
	
	public void setName(String name) { this.name = name; }
	public void setAccount(String account) { this.account = account; }
	public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
	public void setDueDate(int dueDate) { this.dueDate = dueDate; }
	public void setPayAmount(double payAmount) { this.payAmount = payAmount; }
	
	public static boolean validateInput(String name, String account, String transactionType, int dueDateStr, double payAmountStr) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Schedule's name is required.");
            return false;
        }
        if (account == null || account.trim().isEmpty()) {
            System.out.println("Error: Account selection is required.");
            return false;
        }
        if (transactionType == null || transactionType.trim().isEmpty()) {
            System.out.println("Error: Transaction type is required.");
            return false;
        }
        if (isDuplicateName(name)) {
            System.out.println("Error: Schedule's name must be unique.");
            return false;
        }
        return true;
    }
	
	private static boolean isDuplicateName(String name) {
        for (ScheduledTransaction scheduledTransaction : schedTransactionList) {
            if (scheduledTransaction.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
	
	public static void storeScheduledTransaction(String name, String account, String transactionType, int dueDateStr, double payAmountStr) throws IOException {
        if (!validateInput(name, account, transactionType, dueDateStr, payAmountStr)) {
            return;
        }

        ScheduledTransaction newScheduled = new ScheduledTransaction(name, account, transactionType, dueDateStr, payAmountStr);
        schedTransactionList.add(newScheduled);

        File schedTransFile = new File("ScheduledTransactions.csv");
        try (PrintWriter out = new PrintWriter(new FileWriter(schedTransFile, true))) {
            out.println(newScheduled.getName() + "," +
                        newScheduled.getAccount() + "," +
                        newScheduled.getTransactionType() + "," +
                        newScheduled.getFrequency() + "," +
                        newScheduled.getDueDate() + "," +
                        newScheduled.getPayAmount());
        }
    }
	
	public static List<ScheduledTransaction> getAllScheduledTransactions() throws IOException {
	    List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();
	    
	    try (BufferedReader reader = new BufferedReader(new FileReader("ScheduledTransactions.csv"))) {
	        String line = "";
	        
	        while ((line = reader.readLine()) != null) {
	            String[] data = line.split(",");
	            
	            if (data.length == 6) {
	                try {
	                    String scheduleName = data[0].trim();
	                    String accountName = data[1].trim();
	                    String transactionType = data[2].trim();
	                    String frequency = data[3].trim(); 
	                    int dueDate = Integer.parseInt(data[4].trim()); 
	                    double paymentAmount = Double.parseDouble(data[5].trim());
	                    
	                    scheduledTransactions.add(new ScheduledTransaction(scheduleName, accountName, transactionType, dueDate, paymentAmount));
	                    
	                } catch (NumberFormatException e) {
	                    System.out.println("Skipping line due to number format error: " + line);
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	    
	    return scheduledTransactions;
	}
	
	private static void updateScheduledTransaction(ScheduledTransaction updatedTransaction) throws IOException {
	    // Loop through the list of scheduled transactions and find the one to update
	    for (int i = 0; i < schedTransactionList.size(); i++) {
	        ScheduledTransaction st = schedTransactionList.get(i);
	        // Assuming you want to match on a unique field like name and account, modify as needed
	        if (st.getName().equals(updatedTransaction.getName()) && 
	            st.getAccount().equals(updatedTransaction.getAccount())) {
	            
	            // Update the fields of the found scheduled transaction with the updated values
	            schedTransactionList.set(i, updatedTransaction);
	            break; // Once we find and update the transaction, exit the loop
	        }
	    }

	    // Rewrite the file with the updated list of scheduled transactions
	    try (PrintWriter out = new PrintWriter(new FileWriter("ScheduledTransactions.csv"))) {
	        for (ScheduledTransaction st : schedTransactionList) {
	            out.println(st.getName() + "," +
	                        st.getAccount() + "," +
	                        st.getTransactionType() + "," +
	                        st.getFrequency() + "," +
	                        st.getDueDate() + "," +
	                        st.getPayAmount());
	        }
	    }
	}


	@Override
    public List<String> getEditableFields() {
        List<String> fields = new ArrayList<>();
        fields.add(getName()); // Display and edit name
        fields.add(getAccount()); // Display and edit account
        fields.add(String.valueOf(getPayAmount())); // Display and edit payment amount
        // Skip frequency as it's hard-coded
        return fields;
    }
	
	@Override
    public void save() throws IOException {
        // Save updated scheduled transaction to the file
        // Here we update the scheduled transaction object in the list, and rewrite the file
        updateScheduledTransaction(this);
    }
}
