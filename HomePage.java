package application;

import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.TableCell;
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
    private Button scheduleTransactionBtn = new Button("Schedule Transaction");
    private Button viewScheduledTransactionsBtn = new Button("View All Scheduled Transactions");
    private Button viewTransactionsBtn = new Button("View all Transactions");
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

            VBox centerContent = new VBox(15, homeBtn, viewAccountsBtn, transactionTypeBtn, transactionsBtn, scheduleTransactionBtn, viewTransactionsBtn, viewScheduledTransactionsBtn);
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
            scheduleTransactionBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> openScheduleTransactionPage(primary));
            viewTransactionsBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> displayTransactions(primary));
            viewScheduledTransactionsBtn.setOnAction(e -> displayScheduledTransactions(primary));

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
    
    private void openScheduleTransactionPage(Stage primary) {
        // Create the main layout container
        GridPane schedulePane = new GridPane();

        // Page title and labels
        Text scheduleTransactionText = new Text("Schedule a New Transaction");

        // Schedule Name Field
        Label scheduleNameLabel = new Label("Schedule Name:");
        TextField scheduleNameField = new TextField();
        scheduleNameField.setPromptText("Enter schedule name (e.g., Rent, Mortgage)");

        // Account Selection
        Label accountLabel = new Label("Select Account:");
        ComboBox<String> accountComboBox = new ComboBox<>();
        ArrayList<String> accounts = CSVUtils.readAccountsFromCSV("accounts.csv");
        accountComboBox.getItems().addAll(accounts);
        accountComboBox.setValue(accounts.isEmpty() ? "" : accounts.get(0)); 

        // Transaction Type Selection
        Label transTypeLabel = new Label("Select Transaction Type:");
        ComboBox<String> transTypeComboBox = new ComboBox<>();
        ArrayList<String> transactionTypes = CSVUtils.readTransactionTypesFromCSV("transactionTypes.csv");
        transTypeComboBox.getItems().addAll(transactionTypes);
        transTypeComboBox.setValue(transactionTypes.isEmpty() ? "" : transactionTypes.get(0)); 

        // Frequency Field (hard-coded to Monthly)
        Label frequencyLabel = new Label("Frequency:");
        ComboBox<String> frequencyComboBox = new ComboBox<>();
        frequencyComboBox.getItems().add("Monthly");
        frequencyComboBox.setValue("Monthly");

        // Due Date Field
        Label dueDateLabel = new Label("Due Date (Day of Month):");
        TextField dueDateField = new TextField();
        dueDateField.setPromptText("Enter day of month (e.g., 15, 30)");

        // Payment Amount Field
        Label paymentLabel = new Label("Payment Amount:");
        TextField paymentField = new TextField();
        paymentField.setPromptText("Enter payment amount");

        // Button to save the schedule
        Button scheduleTransactionBtn = new Button("Schedule Transaction");
        Button backBtn = new Button("Back to Home");

        // Label to display warnings or success messages
        Label scheduleWarning = new Label();
        scheduleWarning.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        scheduleWarning.setTextFill(Color.RED);

        // Layout setup
        scheduleTransactionText.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        schedulePane.add(scheduleNameLabel, 0, 0);
        schedulePane.add(scheduleNameField, 1, 0);
        schedulePane.add(accountLabel, 0, 1);
        schedulePane.add(accountComboBox, 1, 1);
        schedulePane.add(transTypeLabel, 0, 2);
        schedulePane.add(transTypeComboBox, 1, 2);
        schedulePane.add(frequencyLabel, 0, 3);
        schedulePane.add(frequencyComboBox, 1, 3);
        schedulePane.add(dueDateLabel, 0, 4);
        schedulePane.add(dueDateField, 1, 4);
        schedulePane.add(paymentLabel, 0, 5);
        schedulePane.add(paymentField, 1, 5);

        schedulePane.setHgap(10);
        schedulePane.setVgap(10);
        schedulePane.setPadding(new Insets(10, 10, 10, 10));

        VBox buttonContainer = new VBox(20, scheduleWarning, scheduleTransactionBtn, backBtn);
        buttonContainer.setAlignment(Pos.BOTTOM_CENTER);
        buttonContainer.setPadding(new Insets(10, 0, 20, 0));

        // Add components to the scene
        VBox centerContent = new VBox(20, scheduleTransactionText);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(20, 0, 0, 0));

        schedulePane.setAlignment(Pos.CENTER);
        VBox mainContent = new VBox(30, centerContent, schedulePane, buttonContainer);
        mainContent.setAlignment(Pos.CENTER);

        // Page setup
        pages.setCenter(mainContent);

        // Back button event handler
        backBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));

        // Schedule Transaction button event handler
        scheduleTransactionBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            // Validate inputs
            String scheduleName = scheduleNameField.getText().trim();
            String dueDateText = dueDateField.getText().trim();
            String paymentText = paymentField.getText().trim();

            if (scheduleName.isEmpty() || dueDateText.isEmpty() || paymentText.isEmpty()) {
                scheduleWarning.setText("All fields are required.");
                return;
            }

            /* Check for duplicate schedule names
            ArrayList<String> existingSchedules = CSVUtils.readSchedulesFromCSV("schedules.csv");
            if (existingSchedules.contains(scheduleName)) {
                scheduleWarning.setText("Schedule name already exists.");
                return;
            } */

            // Validate the payment and due date fields
            double paymentAmount = 0.0;
            int dueDate = 0;

            // Validate payment amount
            try {
                paymentAmount = Double.parseDouble(paymentText);
            } catch (NumberFormatException ex) {
                scheduleWarning.setText("Please enter a valid number for Payment Amount.");
                return;
            }

            // Validate due date (ensure it's an integer)
            try {
                dueDate = Integer.parseInt(dueDateText);
            } catch (NumberFormatException ex) {
                scheduleWarning.setText("Please enter a valid integer for Due Date.");
                return;
            }

            // Create the new scheduled transaction
            ScheduledTransaction scheduledTransaction = new ScheduledTransaction(
                scheduleName,
                accountComboBox.getValue(),
                transTypeComboBox.getValue(),
                dueDate,
                paymentAmount
            );

            // Store the transaction
            try {
                ScheduledTransaction.storeScheduledTransaction(scheduleName, accountComboBox.getValue(),transTypeComboBox.getValue(),dueDate,paymentAmount);
                scheduleWarning.setTextFill(Color.GREEN);
                scheduleWarning.setText("Transaction scheduled successfully!");

                // Clear the fields after successful scheduling
                scheduleNameField.clear();
                dueDateField.clear();
                paymentField.clear();
            } catch (IOException ex) {
                scheduleWarning.setText("Error storing transaction. Please try again.");
            }
        });

        // Show the page
        primary.show();
    }


    private void displayTransactions(Stage primary) {
        TableView<Transaction> table = new TableView<>();

        // Column for Account
        TableColumn<Transaction, String> accountCol = new TableColumn<>("Account");
        accountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccount()));

        // Column for Transaction Type
        TableColumn<Transaction, String> transTypeCol = new TableColumn<>("Transaction Type");
        transTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionType()));

        // Column for Transaction Date
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Transaction Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionDate().toString()));

        // Column for Description
        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        // Column for Payment Amount
        TableColumn<Transaction, Double> paymentCol = new TableColumn<>("Payment Amount");
        paymentCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPaymentAmount()).asObject());

        // Column for Deposit Amount
        TableColumn<Transaction, Double> depositCol = new TableColumn<>("Deposit Amount");
        depositCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getDepositAmount()).asObject());

        // Adding columns to the table
        table.getColumns().addAll(accountCol, transTypeCol, dateCol, descCol, paymentCol, depositCol);

        // Fetch the transactions and populate the table
        try {
            List<Transaction> transactions = Transaction.getAllTransactions();
            ObservableList<Transaction> transactionList = FXCollections.observableList(transactions);
            table.setItems(transactionList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort the table by Transaction Date (descending)
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(dateCol);

        // Set up the layout and display the page
        BorderPane transactionPage = new BorderPane();
        transactionPage.setCenter(table);

        // Back button to go to the home page
        Button back = new Button("Back");
        back.setOnAction(e -> backToHomePage(primary));
        VBox bottomContent = new VBox(back);
        bottomContent.setAlignment(Pos.CENTER);
        bottomContent.setPadding(new Insets(15));
        transactionPage.setBottom(bottomContent);

        // Create the scene for the transaction page and display it
        Scene transactionScene = new Scene(transactionPage, 1280, 800);
        primary.setScene(transactionScene);
        primary.show();
    }

    
    private void displayScheduledTransactions(Stage primary) {
        // Create a TableView for displaying scheduled transactions
        TableView<ScheduledTransaction> table = new TableView<>();

        // Define the columns for the table
        TableColumn<ScheduledTransaction, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<ScheduledTransaction, String> accountCol = new TableColumn<>("Account");
        accountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccount()));

        TableColumn<ScheduledTransaction, String> transTypeCol = new TableColumn<>("Transaction Type");
        transTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionType()));

        TableColumn<ScheduledTransaction, String> freqCol = new TableColumn<>("Frequency");
        freqCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFrequency()));

        TableColumn<ScheduledTransaction, Integer> dueDateCol = new TableColumn<>("Due Date");
        // Convert due date to string (day of the month)
        dueDateCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getDueDate()).asObject());

        TableColumn<ScheduledTransaction, Double> paymentCol = new TableColumn<>("Payment Amount");
        paymentCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPayAmount()).asObject());
        // Formatting the payment to 2 decimal places
        paymentCol.setCellFactory(column -> {
            return new TableCell<ScheduledTransaction, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item)); // Format as currency (2 decimal places)
                    }
                }
            };
        });

        // Add columns to the table
        table.getColumns().addAll(nameCol, accountCol, transTypeCol, freqCol, dueDateCol, paymentCol);

        try {
            // Fetch all scheduled transactions and display them in the table
            List<ScheduledTransaction> scheduledTransactions = ScheduledTransaction.getAllScheduledTransactions();
            ObservableList<ScheduledTransaction> scheduledTransactionsList = FXCollections.observableList(scheduledTransactions);
            table.setItems(scheduledTransactionsList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set sorting by Due Date
        dueDateCol.setSortType(TableColumn.SortType.ASCENDING);
        table.getSortOrder().add(dueDateCol);

        // Create a BorderPane for the page layout
        BorderPane scheduledTransactionPage = new BorderPane();
        scheduledTransactionPage.setCenter(table);

        // Create a Back button
        Button back = new Button("Back");
        back.setOnAction(e -> backToHomePage(primary));

        // Create bottom content with the Back button
        VBox bottomContent = new VBox(back);
        bottomContent.setAlignment(Pos.CENTER);
        bottomContent.setPadding(new Insets(15));
        scheduledTransactionPage.setBottom(bottomContent);

        // Set the scene for the scheduled transactions page
        Scene scheduledTransactionScene = new Scene(scheduledTransactionPage, 1280, 800);
        primary.setScene(scheduledTransactionScene);
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
