package application;

import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.application.Application;
import javafx.scene.Node;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javafx.application.Application;
import javafx.scene.Node;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.util.*;

public class HomePage extends Application{
    private BorderPane pages = new BorderPane();
    private Button homeBtn = new Button("Create an account");
    private Button viewAccountsBtn = new Button("View Accounts");
    private Button transactionTypeBtn = new Button("Create Transaction Type");
    private Button transactionsBtn = new Button("Create new Transactions");
    private Text title = new Text("PennyPal");

    @Override
    public void start(Stage primary) {
        try {
            Text welcome = new Text("Welcome to");
            
            title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
            welcome.setStyle("-fx-font-size: 25px;");
            homeBtn.setMinSize(150, 35);
            viewAccountsBtn.setMinSize(150, 35);
            transactionTypeBtn.setMinSize(150, 35);
            transactionsBtn.setMinSize(150, 35);

            VBox centerContent = new VBox(15, homeBtn, viewAccountsBtn, transactionTypeBtn, transactionsBtn);
            centerContent.setAlignment(Pos.CENTER);
            centerContent.setPadding(new Insets(-100, 0, 0, 0));
            
            VBox topContent = new VBox(10, welcome, title);
            topContent.setAlignment(Pos.TOP_CENTER);
            topContent.setPadding(new Insets(150, 0, 0, 0));
            
            pages.setTop(topContent);
            pages.setCenter(centerContent);
            

            viewAccountsBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> displayAccounts(primary));
            homeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> openAccountPage(primary));
            transactionTypeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> openTransactionTypePage(primary));
            transactionsBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> openTransactionsPage(primary));

            Scene homeScene = new Scene(pages,1280,800);
            homeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
            primary.setScene(homeScene);
            primary.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void displayAccounts(Stage primary){
        TableView<Account> table = new TableView<>();
        TableColumn<Account, String> nameCol = new TableColumn<>("Account Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Account, String> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(data -> {
        	DecimalFormat df = new DecimalFormat("#,##0.00");
        	String formattedBalance = df.format(data.getValue().getBalance());
        	return new SimpleStringProperty(formattedBalance);
        });

        TableColumn<Account, String> dateCol = new TableColumn<>("Opening Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOpenDate()));

        table.getColumns().addAll(nameCol, balanceCol, dateCol);

        try{
            List<Account> accounts = Account.getSortedAccountsByDate();
            ObservableList<Account> accountList = FXCollections.observableList(accounts);
            table.setItems(accountList);
        }
        catch(IOException e){
            System.out.println("No accounts recorded yet!");
        }

        BorderPane accountPage = new BorderPane();
        accountPage.setCenter(table);

        Button back = new Button("Home");
        back.setOnAction(e -> backToHomePage(primary));

        VBox bottomContent = new VBox(back);
        bottomContent.setAlignment(Pos.CENTER);
        bottomContent.setPadding(new Insets(15));

        accountPage.setBottom(bottomContent);

        Scene accountScene = new Scene(accountPage, 1280, 800);
        primary.setScene(accountScene);
        primary.show();

    }

    private void openAccountPage(Stage primary) {
        GridPane ap = new GridPane();

        Text createAccount = new Text("Please create account:");
        TextField accName = new TextField();
        TextField accDate = new TextField();
        TextField accBal = new TextField();
        Label nameLabel = new Label("Account name:");
        Label dateLabel = new Label("Opening date:");
        Label balanceLabel = new Label("Opening balance:");
        Button home = new Button("Home");
        Button create = new Button("Create");
        Label warning = new Label();
        create.setMinSize(100, 30);
        
        nameLabel.setStyle("-fx-font-size: 15;");
        dateLabel.setStyle("-fx-font-size: 15;");
        balanceLabel.setStyle("-fx-font-size: 15;");
        createAccount.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        warning.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        warning.setTextFill(Color.RED);
        
        ap.add(accName, 0, 1);
        ap.add(accDate, 1, 1);
        ap.add(accBal, 2, 1);
        ap.add(nameLabel, 0, 0);
        ap.add(dateLabel, 1, 0);
        ap.add(balanceLabel, 2, 0);
        
        ap.setHgap(250);
        ap.setVgap(5);
        ap.setAlignment(Pos.BOTTOM_CENTER);
        ap.setPadding(new Insets(0, 70, 70, 0));
        
        
        VBox centerContent = new VBox(175, title, createAccount);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(194, 0, 0, 0));
        
        VBox createButtonContainer = new VBox(25, warning, create, home);
        createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
        createButtonContainer.setPadding(new Insets(0, 0, 100, 0));
  
        pages.setTop(centerContent);
        pages.setCenter(ap);
        pages.setBottom(createButtonContainer);

        home.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));
        
        // Gathers data from the 3 text fields, creates a new account with that data and stores it in Accounts.csv
        create.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        	String name = accName.getText();
        	String openDate = accDate.getText();
            String balanceText = accBal.getText();
            
            if (name.isEmpty()) {
            	warning.setText("Please insert name");
            	return;
            }
            
            if (!openDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
            	warning.setText("Invalid date format. Please use MM/dd/yyyy format.");
            	return;
            }
            
            double bal;
            try {
            	bal = Double.parseDouble(balanceText);
            } catch (NumberFormatException ex) {
            	warning.setText("Invalid balance input. Please enter a numeric value.");
            	return;
            }
            
            try {
            	Account newAcc = new Account(name, openDate, bal);
                warning.setTextFill(Color.GREEN);
                warning.setText("New account: " + newAcc.getName() + " created successfully.");
                Account.storeData(newAcc);
                
                accName.clear();
                accDate.clear();
                accBal.clear();
                
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        primary.show();

    }

    private void backToHomePage(Stage primary) {
        HomePage hp = new HomePage();
        hp.start(primary);
    }

    private void openTransactionTypePage(Stage primary) {
    	GridPane ttp = new GridPane();
    	
    	Text createTT = new Text("Please create new transaction type");
    	Label transTypeLabel = new Label("Enter Transaction type:");
    	
        TextField transTypeName = new TextField();
        Button createTTBtn = new Button("Create");
        Button back = new Button("Home");
        Label warning = new Label();
        
        createTT.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        transTypeLabel.setStyle("-fx-font-size: 15;");
        warning.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        warning.setTextFill(Color.RED);
        
        
        ttp.add(transTypeName, 1, 1);
        ttp.add(transTypeLabel, 1, 0);
        
        ttp.setHgap(75);
        ttp.setVgap(5);
        ttp.setAlignment(Pos.BOTTOM_CENTER);
        ttp.setPadding(new Insets(0, 70, 70, 0));
        
        VBox centerContent = new VBox(115, title, createTT);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(194, 0, 0, 0));
        
        VBox createButtonContainer = new VBox(25, warning, createTTBtn, back);
        createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
        createButtonContainer.setPadding(new Insets(0, 0, 100, 0));
        
        pages.setCenter(ttp);
        pages.setTop(centerContent);
        pages.setBottom(createButtonContainer);
        
        createTTBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        	String typeName = transTypeName.getText().trim();

            if(typeName.isEmpty()){
                warning.setText("Please enter a transaction type name.");
                return;

            }
            
            try{
                TransactionType newType = new TransactionType(typeName);
                TransactionType.storeTransactionType(newType);
                warning.setTextFill(Color.GREEN);
                warning.setText("Transaction type '" + typeName + "' create successfully.");
                
                transTypeName.clear();

            }
            catch(IOException ex){
                warning.setText("Error storing transaction type. PLease try again");
                ex.printStackTrace();
            }
        	
        });
        
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));
        
        primary.show();
        
    }
    
    private void openTransactionsPage(Stage primary) {
        GridPane transactionPane = new GridPane();

        Text createTransactionText = new Text("Create New Transaction");

        Label accountLabel = new Label("Select Account:");
        ComboBox<String> accountComboBox = new ComboBox<>();
        ArrayList<String> accounts = CSVUtils.readAccountsFromCSV("accounts.csv");
        accountComboBox.getItems().addAll(accounts);
        accountComboBox.setValue(accounts.isEmpty() ? "" : accounts.get(0)); 

        Label transTypeLabel = new Label("Select Transaction Type:");
        ComboBox<String> transTypeComboBox = new ComboBox<>();
        ArrayList<String> transactionTypes = CSVUtils.readTransactionTypesFromCSV("transactionTypes.csv");
        transTypeComboBox.getItems().addAll(transactionTypes);
        transTypeComboBox.setValue(transactionTypes.isEmpty() ? "" : transactionTypes.get(0)); 

        Label transDateLabel = new Label("Transaction Date:");
        DatePicker transDatePicker = new DatePicker(LocalDate.now()); 

        Label transDescLabel = new Label("Enter Description:");
        TextField transDescField = new TextField();

        Label paymentLabel = new Label("Payment Amount:");
        TextField paymentField = new TextField();

        Label depositLabel = new Label("Deposit Amount:");
        TextField depositField = new TextField();

        Button createTransactionBtn = new Button("Create");
        Button backBtn = new Button("Home");

        Label transactionWarning = new Label();
        transactionWarning.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        transactionWarning.setTextFill(Color.RED);

        createTransactionText.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        transactionPane.add(accountLabel, 0, 0);
        transactionPane.add(accountComboBox, 1, 0);
        transactionPane.add(transTypeLabel, 0, 1);
        transactionPane.add(transTypeComboBox, 1, 1);
        transactionPane.add(transDateLabel, 0, 2);
        transactionPane.add(transDatePicker, 1, 2);
        transactionPane.add(transDescLabel, 0, 3);
        transactionPane.add(transDescField, 1, 3);
        transactionPane.add(paymentLabel, 0, 4);
        transactionPane.add(paymentField, 1, 4);
        transactionPane.add(depositLabel, 0, 5);
        transactionPane.add(depositField, 1, 5);

        transactionPane.setHgap(75);
        transactionPane.setVgap(5);
        transactionPane.setAlignment(Pos.BOTTOM_CENTER);
        transactionPane.setPadding(new Insets(0, 70, 70, 0));

        VBox centerContent = new VBox(115, title, createTransactionText);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(194, 0, 0, 0));

        VBox createButtonContainer = new VBox(25, transactionWarning, createTransactionBtn, backBtn);
        createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
        createButtonContainer.setPadding(new Insets(0, 0, 100, 0));

        pages.setCenter(transactionPane);
        pages.setTop(centerContent);
        pages.setBottom(createButtonContainer);

        createTransactionBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            String description = transDescField.getText().trim();
            String paymentText = paymentField.getText().trim();
            String depositText = depositField.getText().trim();

            if (description.isEmpty() || 
                (paymentText.isEmpty() && depositText.isEmpty())) {
                transactionWarning.setText("Please fill in the required fields.");
                return;
            }

            double payment = 0.0;
            double deposit = 0.0;

            try {
                if (!paymentText.isEmpty()) {
                    payment = Double.parseDouble(paymentText);
                }
                if (!depositText.isEmpty()) {
                    deposit = Double.parseDouble(depositText);
                }
            } catch (NumberFormatException ex) {
                transactionWarning.setText("Please enter valid numbers for Payment and Deposit amounts.");
                return;
            }

            Transaction newTransaction = new Transaction(
                accountComboBox.getValue(),
                transTypeComboBox.getValue(),
                transDatePicker.getValue(),
                description,
                payment,
                deposit
            );

            try {
                Transaction.storeTransaction(newTransaction);
                transactionWarning.setTextFill(Color.GREEN);
                transactionWarning.setText("Transaction created successfully.");

                transDescField.clear();
                paymentField.clear();
                depositField.clear();

            } catch (IOException ex) {
                transactionWarning.setText("Error storing transaction. Please try again.");
                ex.printStackTrace();
            }
        });

        backBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));

        primary.show();
    }
    
    public class CSVUtils {
    	public static ArrayList<String> readAccountsFromCSV(String filename) {
    	    ArrayList<String> accounts = new ArrayList<>();
    	    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
    	        String line;
    	        while ((line = br.readLine()) != null) {
    	            String[] values = line.split(","); 
    	            if (values.length > 0) {
    	                accounts.add(values[0].trim()); 
    	            }
    	        }
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	    return accounts;
    	}

        public static ArrayList<String> readTransactionTypesFromCSV(String filename) {
            ArrayList<String> transactionTypes = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    transactionTypes.add(line.trim()); 
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return transactionTypes;
        }
    }
}
