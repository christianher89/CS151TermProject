package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.*;

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
    public double getBalance(){
        return startBal;
    }
    public String getOpenDate(){
        return openDate;
    }

    public static void storeData(Account newAcc) throws FileNotFoundException {
        // TODO Auto-generated method stub
        File accounts = new File("Accounts.csv");
        PrintWriter out = new PrintWriter(accounts);

        out.print(newAcc);
        out.close();

    }
    public static void appndData(Account newAcc) throws FileNotFoundException{
        File accounts = new File("Account.csv");
        PrintWriter out = new PrintWriter(new FileOutputStream(accounts, true));

        out.println(newAcc.getName() + "," + newAcc.getBalance() + "," + newAcc.getOpenDate());
        out.close();
    }
    public static List<Account> getAllAccounts() throws FileNotFoundException{
        List<Account> accounts = new ArrayList<>();
        try(Scanner sc = new Scanner(new File("Accounts.csv"))){
            while(sc.hasNextLine()){
                String[] data = sc.nextLine().split(",");
                if(data.length == 3) accounts.add(new Account(data[0], Double.parseDouble(data[1]), data[2]));

            }
        }
        return accounts;
    }

    public static List<Account> getSortedAccountsByDate() throws FileNotFoundException{
        List<Account> accounts = getAllAccounts();
        accounts.sort(Comparator.comparing(Account::getOpenDate).reversed());
        return accounts;
    }
}
