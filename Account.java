package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.*;

public class Account {
    private String accName = "";
    private double startBal = 0;
    private LocalDate openDate;
    private static ArrayList<Account> accountList = new ArrayList<>();

    public Account(String name, LocalDate date, double bal) { 
        accName = name;
        openDate = date;
        startBal = bal;
    }

    public String getName() {
        return accName;
    }

    public double getBalance() {
        return startBal;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public static void storeData(Account newAcc) throws IOException {
        accountList.add(newAcc);

        File accounts = new File("Accounts.csv");
        PrintWriter out = new PrintWriter(accounts);

        for (Account a : accountList) {
            out.println(a.getName() + "," + a.getBalance() + "," + a.getOpenDate());
        }
        out.close();
    }
    
    public static List<Account> getAllAccounts() throws IOException {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Accounts.csv"))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    try {
                        String name = data[0].trim();
                        LocalDate date = LocalDate.parse(data[2].trim());
                        double balance = Double.parseDouble(data[1].trim());
                        accounts.add(new Account(name, date, balance));
                    } catch (Exception e) {
                        System.out.println("Skipping line due to error: " + line);
                        e.printStackTrace();
                    }
                }
            }
        }
        return accounts;
    }

    public static List<Account> getSortedAccountsByDate() throws IOException {
        List<Account> accounts = getAllAccounts();
        accounts.sort((a1, a2) -> a2.getOpenDate().compareTo(a1.getOpenDate()));
        return accounts;
    }
}
