package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Account {
	private String accName = "";
    private double startBal = 0;
    private String openDate;
    private static ArrayList<Account> accountList = new ArrayList<Account>();

    public Account (String name, String date, double bal) {
        accName = name;
        openDate = date;
        startBal = bal;
    }

    public String getName() {
        return accName;
    }
    
    public double getBalance(){
        return startBal;
    }
    
    public String getOpenDate(){
        return openDate;
    }

    public static void storeData(Account newAcc) throws IOException {
        // TODO Auto-generated method stub
    	accountList.add(newAcc);
    	
        File accounts = new File("Accounts.csv");
        PrintWriter out = new PrintWriter(accounts);
        
        for (Account a : accountList) {
        	out.println(a.getName() + "," + a.getBalance() + "," + a.getOpenDate());
        }
        out.close();

    }
    
    public static List<Account> getAllAccounts() throws IOException{
        List<Account> accounts = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader("Accounts.csv"))){
        	String line = "";
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data.length == 3) {
                	try {
                		String name = data[0].trim();
                		String date = data[2].trim();
                		double balance = Double.parseDouble(data[1].trim());
                		accounts.add(new Account(name, date, balance));
                	} catch (NumberFormatException e) {
                		System.out.println("Skipping line due to number format error:" + line);
                		e.printStackTrace();
                	}
                }

            }
        }
        return accounts;
    }

    public static List<Account> getSortedAccountsByDate() throws IOException {
        List<Account> accounts = getAllAccounts();
        
        // Use Collections.sort with a custom comparator for descending order
        Collections.sort(accounts, new Comparator<Account>() {
            @Override
            public int compare(Account a1, Account a2) {
                String[] date1 = a1.getOpenDate().split("/");
                String[] date2 = a2.getOpenDate().split("/");
                
                // Compare year first, then month, then day in reverse order
                int yearCompare = Integer.compare(Integer.parseInt(date2[2]), Integer.parseInt(date1[2]));
                if (yearCompare != 0) return yearCompare;
                
                int monthCompare = Integer.compare(Integer.parseInt(date2[0]), Integer.parseInt(date1[0]));
                if (monthCompare != 0) return monthCompare;
                
                return Integer.compare(Integer.parseInt(date2[1]), Integer.parseInt(date1[1])); // Compare days
            }
        });

        return accounts;
    }
}