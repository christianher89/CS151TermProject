package application;
import java.io.*;
import java.io.PrintWriter;

public class TransactionType {
    private String name = "";

    public TransactionType(String typeName) {
        name = typeName;
    }

    public String getName() {
        return name;
    }
    public static void storeTransactionType(TransactionType newType) throw IOException{
        File typesFile = new File("TransactionTypes.cvs");
        try (PrintWriter out = new PrintWriter(new FileWriter(typesFile, true))){
            out.println(newType.getName());
        }
    }
}
