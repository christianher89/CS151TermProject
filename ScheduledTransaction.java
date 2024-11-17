
package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ScheduledTransaction {
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

    public static void storeScheduledTransaction(String name, String account, String transactionType, LocalDate dueDate, double payAmountStr) throws IOException {

//         if (!validateInput(name, account, transactionType, dueDateStr, payAmountStr)) {
//            return;
//        }

        ScheduledTransaction newScheduled = new ScheduledTransaction(name, account, transactionType, dueDate, payAmountStr);
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

                // Make sure each line contains the right number of fields
                if (data.length == 6) {
                    try {
                        String scheduleName = data[0].trim(); // Schedule's name
                        String accountName = data[1].trim();  // Account Name
                        String transactionType = data[2].trim(); // Transaction Type
                        String frequency = data[3].trim(); // Frequency (hardcoded as "Monthly" in the UI)
                        int dueDate = Integer.parseInt(data[4].trim()); // Due Date (integer)
                        double paymentAmount = Double.parseDouble(data[5].trim()); // Payment Amount

                        // Create and add the ScheduledTransaction to the list
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


}
