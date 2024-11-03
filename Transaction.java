package application;

import java.io.*;
import java.util.ArrayList;
import java.time.LocalDate;

public class Transaction {
    private String account;
    private String transactionType;
    private LocalDate transactionDate;
    private String description;
    private double paymentAmount;
    private double depositAmount;
    
    private static ArrayList<Transaction> transactionList = new ArrayList<>();

    public Transaction(String account, String transactionType, LocalDate transactionDate,
                       String description, double paymentAmount, double depositAmount) {
        this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.description = description;
        this.paymentAmount = paymentAmount;
        this.depositAmount = depositAmount;
    }

    // Getters for each field (optional)
    public String getAccount() { return account; }
    public String getTransactionType() { return transactionType; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public String getDescription() { return description; }
    public double getPaymentAmount() { return paymentAmount; }
    public double getDepositAmount() { return depositAmount; }

    public static void storeTransaction(Transaction newTransaction) throws IOException {
        transactionList.add(newTransaction);

        File transactionFile = new File("Transactions.csv");
        try (PrintWriter out = new PrintWriter(new FileWriter(transactionFile, true))) {
            out.println(newTransaction.getAccount() + "," +
                        newTransaction.getTransactionType() + "," +
                        newTransaction.getTransactionDate() + "," +
                        newTransaction.getDescription() + "," +
                        newTransaction.getPaymentAmount() + "," +
                        newTransaction.getDepositAmount());
        }
    }
}
