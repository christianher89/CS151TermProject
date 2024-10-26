package application;

import java.io.File;
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
    
    public static void appendData(Account newAcc) throws IOException{
        File accounts = new File("Account.csv");
        PrintWriter out = new PrintWriter(new FileOutputStream(accounts, true));

        out.println(newAcc.getName() + "," + newAcc.getBalance() + "," + newAcc.getOpenDate());
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

    public static List<Account> getSortedAccountsByDate() throws IOException{
        List<Account> accounts = getAllAccounts();
        accounts.sort(Comparator.comparing(Account::getOpenDate).reversed());
        return accounts;
    }
}
