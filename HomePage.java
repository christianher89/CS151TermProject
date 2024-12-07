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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javafx.application.Application;
import javafx.scene.Node;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.util.*;

public class HomePage extends Application {
	private BorderPane pages = new BorderPane();
	private Button homeBtn = new Button("Create an account");
	private Button viewAccountsBtn = new Button("View Accounts");
	private Button transactionTypeBtn = new Button("Create Transaction Type");
	private Button transactionsBtn = new Button("Create new Transactions");
	private Button scheduleTransactionBtn = new Button("Schedule Transaction");
	private Button viewScheduledTransactionsBtn = new Button("View All Scheduled Transactions");
	private Button viewTransactionsBtn = new Button("View all Transactions");
	private Button accReportBtn = new Button("Account Report");
	private Button tTReportBtn = new Button("Transaction Type Report");
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
			scheduleTransactionBtn.setMinSize(150, 35);
			viewScheduledTransactionsBtn.setMinSize(150, 35);
			viewTransactionsBtn.setMinSize(150, 35);
			accReportBtn.setMinSize(150, 35);
			tTReportBtn.setMinSize(150, 35);

			VBox centerContent = new VBox(15, homeBtn, viewAccountsBtn, transactionTypeBtn, transactionsBtn,
					scheduleTransactionBtn, viewTransactionsBtn, viewScheduledTransactionsBtn, accReportBtn, tTReportBtn);
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
			accReportBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> displayAccReportPage(primary));
			tTReportBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> displayTTReportPage(primary));

			Scene homeScene = new Scene(pages, 1280, 800);
			homeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primary.setScene(homeScene);
			primary.show();
			
			notifyDueTransactions();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void displayAccounts(Stage primary) {
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
		dateCol.setCellValueFactory(data -> {
			LocalDate date = data.getValue().getOpenDate();
			return new SimpleStringProperty(date != null ? date.toString() : "N/A");
		});

		table.getColumns().addAll(nameCol, balanceCol, dateCol);

		try {
			List<Account> accounts = Account.getSortedAccountsByDate();
			ObservableList<Account> accountList = FXCollections.observableList(accounts);
			table.setItems(accountList);
		} catch (IOException e) {
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
		BorderPane accountPage = new BorderPane();

		GridPane ap = new GridPane();

		Text createAccount = new Text("Please create account:");
		TextField accName = new TextField();
		DatePicker accDate = new DatePicker();
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
		ap.setPadding(new Insets(0, 70, 40, 0));

		VBox centerContent = new VBox(90, title, createAccount);
		centerContent.setAlignment(Pos.TOP_CENTER);
		centerContent.setPadding(new Insets(194, 0, 0, 0));

		VBox createButtonContainer = new VBox(25, warning, create, home);
		createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
		createButtonContainer.setPadding(new Insets(0, 0, 140, 0));

		accountPage.setTop(centerContent);
		accountPage.setCenter(ap);
		accountPage.setBottom(createButtonContainer);

		home.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));

		// Event handler for creating a new account
		create.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			String name = accName.getText();
			LocalDate openDate = accDate.getValue();
			String balanceText = accBal.getText();

			if (name.isEmpty()) {
				warning.setText("Please insert name");
				return;
			}

			if (openDate == null) {
				warning.setText("Please select a date");
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
				accDate.setValue(null);
				accBal.clear();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		Scene accountScene = new Scene(accountPage, 1280, 800);
		primary.setScene(accountScene);
		primary.show();
	}
	
	private void notifyDueTransactions() {
	    try {
	        List<ScheduledTransaction> dueTransactions = ScheduledTransaction.getDueTransactions();
	        if (!dueTransactions.isEmpty()) {
	            Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Scheduled Transactions Due");
	            alert.setHeaderText("Transactions due today:");
	            StringBuilder message = new StringBuilder();
	            for (ScheduledTransaction st : dueTransactions) {
	                message.append("- ").append(st.getName())
	                       .append(" (Account: ").append(st.getAccount())
	                       .append(", Amount: $").append(st.getPayAmount())
	                       .append(")\n");
	            }
	            alert.setContentText(message.toString());
	            alert.showAndWait();
	        }
	    } catch (IOException e) {
	        System.err.println("Error retrieving due transactions: " + e.getMessage());
	    }
	}

	private void backToHomePage(Stage primary) {
		HomePage hp = new HomePage();
		hp.start(primary);
	}

	private void openTransactionTypePage(Stage primary) {
		BorderPane ttpPage = new BorderPane();

		GridPane ttp = new GridPane();

		Text createTT = new Text("Please create new transaction type");
		Label transTypeLabel = new Label("Enter Transaction type:");

		TextField transTypeName = new TextField();
		Button createTTBtn = new Button("Create");
		Button back = new Button("Home");
		Label warning = new Label();
		createTTBtn.setMinSize(80, 10);
		back.setMinSize(40, 10);

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
		ttp.setPadding(new Insets(0, 70, 40, 0));

		VBox centerContent = new VBox(85, title, createTT);
		centerContent.setAlignment(Pos.TOP_CENTER);
		centerContent.setPadding(new Insets(194, 0, 0, 0));

		VBox createButtonContainer = new VBox(25, warning, createTTBtn, back);
		createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
		createButtonContainer.setPadding(new Insets(0, 0, 125, 0));

		ttpPage.setCenter(ttp);
		ttpPage.setTop(centerContent);
		ttpPage.setBottom(createButtonContainer);

		createTTBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			String typeName = transTypeName.getText().trim();

			if (typeName.isEmpty()) {
				warning.setText("Please enter a transaction type name.");
				return;

			}

			try {
				File typesFile = new File("TransactionTypes.csv");
				Set<String> existingTypes = new HashSet<>();

				if (typesFile.exists()) {
					BufferedReader reader = new BufferedReader(new FileReader(typesFile));
					String line;
					while ((line = reader.readLine()) != null) {
						existingTypes.add(line.trim());
					}
					reader.close();
				}

				// Check for duplicates
				if (existingTypes.contains(typeName)) {
					warning.setTextFill(Color.RED);
					warning.setText("Transaction type '" + typeName + "' already exists.");
					return;
				}

				TransactionType newType = new TransactionType(typeName);
				TransactionType.storeTransactionType(newType);
				warning.setTextFill(Color.GREEN);
				warning.setText("Transaction type '" + typeName + "' create successfully.");

				transTypeName.clear();

			} catch (IOException ex) {
				warning.setText("Error storing transaction type. PLease try again");
				ex.printStackTrace();
			}

		});

		back.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));

		Scene ttpScene = new Scene(ttpPage, 1280, 800);
		primary.setScene(ttpScene);
		primary.show();

	}

	private void openTransactionsPage(Stage primary) {
		BorderPane transactionPage = new BorderPane();

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
		createTransactionBtn.setMinSize(80, 10);
		backBtn.setMinSize(40, 10);

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
		transactionPane.setPadding(new Insets(0, 70, 30, 0));

		VBox centerContent = new VBox(50, title, createTransactionText);
		centerContent.setAlignment(Pos.TOP_CENTER);
		centerContent.setPadding(new Insets(194, 0, 0, 0));

		VBox createButtonContainer = new VBox(25, transactionWarning, createTransactionBtn, backBtn);
		createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
		createButtonContainer.setPadding(new Insets(0, 0, 80, 0));

		transactionPage.setCenter(transactionPane);
		transactionPage.setTop(centerContent);
		transactionPage.setBottom(createButtonContainer);

		createTransactionBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			String description = transDescField.getText().trim();
			String paymentText = paymentField.getText().trim();
			String depositText = depositField.getText().trim();

			if (description.isEmpty() || (paymentText.isEmpty() && depositText.isEmpty())) {
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

			Transaction newTransaction = new Transaction(accountComboBox.getValue(), transTypeComboBox.getValue(),
					transDatePicker.getValue(), description, payment, deposit);

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

		Scene transactionScene = new Scene(transactionPage, 1280, 800);
		primary.setScene(transactionScene);
		primary.show();
	}

	private void openScheduleTransactionPage(Stage primary) {
		BorderPane scheduleTransactionPage = new BorderPane();

		GridPane schedulePane = new GridPane();
		Text scheduleTransactionText = new Text("Schedule a New Transaction");
		Label scheduleNameLabel = new Label("Schedule Name:");
		TextField scheduleNameField = new TextField();
		scheduleNameField.setPromptText("Enter schedule name (e.g., Rent, Mortgage)");

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

		Label frequencyLabel = new Label("Frequency:");
		ComboBox<String> frequencyComboBox = new ComboBox<>();
		frequencyComboBox.getItems().add("Monthly");
		frequencyComboBox.setValue("Monthly");

		Label dueDateLabel = new Label("Due Date (Day of Month):");
		TextField dueDateField = new TextField();
		dueDateField.setPromptText("Enter day of month (e.g., 15, 30)");

		Label paymentLabel = new Label("Payment Amount:");
		TextField paymentField = new TextField();
		paymentField.setPromptText("Enter payment amount");

		Button scheduleTransactionBtn = new Button("Schedule Transaction");
		Button backBtn = new Button("Back to Home");

		Label scheduleWarning = new Label();
		scheduleWarning.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
		scheduleWarning.setTextFill(Color.RED);

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
		schedulePane.setPadding(new Insets(10, 10, 0, 10));

		VBox buttonContainer = new VBox(20, scheduleWarning, scheduleTransactionBtn, backBtn);
		buttonContainer.setAlignment(Pos.BOTTOM_CENTER);
		buttonContainer.setPadding(new Insets(10, 0, 20, 0));

		VBox centerContent = new VBox(20, title, scheduleTransactionText);
		centerContent.setAlignment(Pos.TOP_CENTER);
		centerContent.setPadding(new Insets(136, 0, 0, 0));

		schedulePane.setAlignment(Pos.CENTER);
		VBox mainContent = new VBox(30, centerContent, schedulePane, buttonContainer);
		mainContent.setAlignment(Pos.CENTER);

		scheduleTransactionPage.setCenter(mainContent);

		backBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));

		scheduleTransactionBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			String scheduleName = scheduleNameField.getText().trim();
			String dueDateText = dueDateField.getText().trim();
			String paymentText = paymentField.getText().trim();

			if (scheduleName.isEmpty() || dueDateText.isEmpty() || paymentText.isEmpty()) {
				scheduleWarning.setText("All fields are required.");
				return;
			}

			try {
				File schedulesFile = new File("ScheduledTransactions.csv");
				Set<String> existingSchedules = new HashSet<>();

				if (schedulesFile.exists()) {
					BufferedReader reader = new BufferedReader(new FileReader(schedulesFile));
					String line;
					while ((line = reader.readLine()) != null) {
						String[] parts = line.split(",");
						if (parts.length > 0) {
							existingSchedules.add(parts[0].trim());
						}
					}
					reader.close();
				}

				if (existingSchedules.contains(scheduleName)) {
					scheduleWarning.setTextFill(Color.RED);
					scheduleWarning.setText("Schedule name '" + scheduleName + "' already exists.");
					return;
				}

				double paymentAmount = Double.parseDouble(paymentText);
				int dueDate = Integer.parseInt(dueDateText);

				ScheduledTransaction scheduledTransaction = new ScheduledTransaction(scheduleName,
						accountComboBox.getValue(), transTypeComboBox.getValue(), dueDate, paymentAmount);

				ScheduledTransaction.storeScheduledTransaction(scheduleName, accountComboBox.getValue(),
						transTypeComboBox.getValue(), dueDate, paymentAmount);

				scheduleWarning.setTextFill(Color.GREEN);
				scheduleWarning.setText("Schedule name '" + scheduleName + "' created successfully!");
				scheduleNameField.clear();
				dueDateField.clear();
				paymentField.clear();
			} catch (IOException ex) {
				scheduleWarning.setTextFill(Color.RED);
				scheduleWarning.setText("Error storing schedule. Please try again.");
				ex.printStackTrace();
			} catch (NumberFormatException ex) {
				scheduleWarning.setTextFill(Color.RED);
				scheduleWarning.setText("Please enter valid numbers for Payment Amount and Due Date.");
			}
		});

		Scene scheduleTransactionScene = new Scene(scheduleTransactionPage, 1280, 800);
		primary.setScene(scheduleTransactionScene);
		primary.show();
	}

	private void displayTransactions(Stage primary) {
		BorderPane transactionPage = new BorderPane();

		Button back = new Button("Home");
		back.setMinSize(50, 10);

		Label tSearchLabel = new Label("Search Transactions:");
		TextField tSearch = new TextField();
		tSearch.setMaxWidth(250);

		VBox searchContainer = new VBox(tSearchLabel, tSearch);

		TableView<Transaction> table = new TableView<>();

		TableColumn<Transaction, String> accountCol = new TableColumn<>("Account");
		accountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccount()));

		TableColumn<Transaction, String> transTypeCol = new TableColumn<>("Transaction Type");
		transTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionType()));

		TableColumn<Transaction, String> dateCol = new TableColumn<>("Transaction Date");
		dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionDate().toString()));

		TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
		descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

		TableColumn<Transaction, Double> paymentCol = new TableColumn<>("Payment Amount");
		paymentCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPaymentAmount()).asObject());

		TableColumn<Transaction, Double> depositCol = new TableColumn<>("Deposit Amount");
		depositCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getDepositAmount()).asObject());

		table.getColumns().addAll(accountCol, transTypeCol, dateCol, descCol, paymentCol, depositCol);

		try {
			List<Transaction> transactions = Transaction.getAllTransactions();
			ObservableList<Transaction> transactionList = FXCollections.observableList(transactions);
			table.setItems(transactionList);

			TableColumn<Transaction, Void> editCol = new TableColumn<>("Edit");
	        editCol.setCellFactory(param -> new TableCell<Transaction, Void>() {
	            private final Button editButton = new Button("Edit");

	            {
	                // On button click, open the edit page
	                editButton.setOnAction(event -> {
	                    Transaction selectedTransaction = getTableView().getItems().get(getIndex());
	                    openEditPage(selectedTransaction, primary);
	                });
	            }

	            @Override
	            public void updateItem(Void item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty) {
	                    setGraphic(null);
	                } else {
	                    setGraphic(editButton);
	                }
	            }
	        });
	        table.getColumns().add(editCol);
			
			tSearch.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
				List<Transaction> filterTransactions = new ArrayList<Transaction>(); 
				String searchSub = tSearch.getText();
				if (tSearch.getText().isEmpty()) {
					table.setItems(transactionList);
					return;
				}
				for (Transaction tran : transactions) {
					if (tran.getDescription().toLowerCase().contains(searchSub.toLowerCase())) {
						filterTransactions.add(tran);
					}
				}

				ObservableList<Transaction> filterList = FXCollections.observableList(filterTransactions);
				table.setItems(filterList);
			});

		} catch (IOException e) {
			System.out.println("No transactions recorded yet!");
		}

		dateCol.setSortType(TableColumn.SortType.DESCENDING);
		table.getSortOrder().add(dateCol);

		transactionPage.setTop(searchContainer);
		transactionPage.setCenter(table);

		back.setOnAction(e -> backToHomePage(primary));

		VBox bottomContent = new VBox(back);
		bottomContent.setAlignment(Pos.CENTER);
		bottomContent.setPadding(new Insets(15));
		transactionPage.setBottom(bottomContent);

		Scene transactionScene = new Scene(transactionPage, 1280, 800);
		primary.setScene(transactionScene);
		primary.show();
	}

	private void displayScheduledTransactions(Stage primary) {
		BorderPane scheduledTransactionPage = new BorderPane();

		Button back = new Button("Home");
		back.setMinSize(50, 10);

		Label stSearchLabel = new Label("Search Scheduled Transactions:");
		TextField stSearch = new TextField();
		stSearch.setMaxWidth(250);
		VBox searchContainer = new VBox(stSearchLabel, stSearch);

		TableView<ScheduledTransaction> table = new TableView<>();

		TableColumn<ScheduledTransaction, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

		TableColumn<ScheduledTransaction, String> accountCol = new TableColumn<>("Account");
		accountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccount()));

		TableColumn<ScheduledTransaction, String> transTypeCol = new TableColumn<>("Transaction Type");
		transTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionType()));

		TableColumn<ScheduledTransaction, String> freqCol = new TableColumn<>("Frequency");
		freqCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFrequency()));

		TableColumn<ScheduledTransaction, Integer> dueDateCol = new TableColumn<>("Due Date");
		dueDateCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getDueDate()).asObject());

		TableColumn<ScheduledTransaction, Double> paymentCol = new TableColumn<>("Payment Amount");
		paymentCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPayAmount()).asObject());
		paymentCol.setCellFactory(column -> {
			return new TableCell<ScheduledTransaction, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(String.format("%.2f", item));
					}
				}
			};
		});

		table.getColumns().addAll(nameCol, accountCol, transTypeCol, freqCol, dueDateCol, paymentCol);

		TableColumn<ScheduledTransaction, Void> editCol = new TableColumn<>("Edit");
	    editCol.setCellFactory(param -> new TableCell<ScheduledTransaction, Void>() {
	        private final Button editButton = new Button("Edit");

	        {
	            // On button click, open the edit page
	            editButton.setOnAction(event -> {
	                ScheduledTransaction selectedST = getTableView().getItems().get(getIndex());
	                openEditPage(selectedST, primary);
	            });
	        }

	        @Override
	        public void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null);
	            } else {
	                setGraphic(editButton);
	            }
	        }
	    });
	    
	    table.getColumns().add(editCol);
		
		try {
			List<ScheduledTransaction> scheduledTransactions = ScheduledTransaction.getAllScheduledTransactions();
			ObservableList<ScheduledTransaction> scheduledTransactionsList = FXCollections
					.observableList(scheduledTransactions);
			table.setItems(scheduledTransactionsList);

			// Update table as letters are typed
			stSearch.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
				List<ScheduledTransaction> filterSTransactions = new ArrayList<ScheduledTransaction>(); // list for
																										// filtered
																										// Transactions
				String searchSub = stSearch.getText();
				if (stSearch.getText().isEmpty()) {
					// If the textField empty, reset the table and jump out
					table.setItems(scheduledTransactionsList);
					return;
				}
				for (ScheduledTransaction st : scheduledTransactions) {
					// Take both strings to lower case for .contains()
					if (st.getName().toLowerCase().contains(searchSub.toLowerCase())) {
						filterSTransactions.add(st);
					}
				}

				ObservableList<ScheduledTransaction> filterList = FXCollections.observableList(filterSTransactions);
				table.setItems(filterList);
			});
		} catch (IOException e) {
			System.out.println("No scheduled transactions recorded yet!");
		}

		dueDateCol.setSortType(TableColumn.SortType.ASCENDING);
		table.getSortOrder().add(dueDateCol);

		scheduledTransactionPage.setCenter(table);

		back.setOnAction(e -> backToHomePage(primary));

		VBox bottomContent = new VBox(back);
		bottomContent.setAlignment(Pos.CENTER);
		bottomContent.setPadding(new Insets(15));
		scheduledTransactionPage.setBottom(bottomContent);
		scheduledTransactionPage.setTop(searchContainer);

		Scene scheduledTransactionScene = new Scene(scheduledTransactionPage, 1280, 800);
		primary.setScene(scheduledTransactionScene);
		primary.show();
	}
	
	private void openEditPage(Editable editableObject, Stage primary) {
	    VBox editLayout = new VBox(10);
	    editLayout.setPadding(new Insets(10));

	    Map<String, Control> fieldMapping = new HashMap<>();

	    Text editText = new Text("Edit " + editableObject.getClass().getSimpleName());
	    editText.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
	    editLayout.getChildren().add(editText);

	    Label accountLabel = new Label("Select Account:");
	    ComboBox<String> accountComboBox = new ComboBox<>();
	    ArrayList<String> accounts = CSVUtils.readAccountsFromCSV("accounts.csv");
	    accountComboBox.getItems().addAll(accounts);

	    Label transTypeLabel = new Label("Select Transaction Type:");
	    ComboBox<String> transTypeComboBox = new ComboBox<>();
	    ArrayList<String> transactionTypes = CSVUtils.readTransactionTypesFromCSV("transactionTypes.csv");
	    transTypeComboBox.getItems().addAll(transactionTypes);

	    Label transDateLabel = new Label("Transaction Date:");
	    DatePicker transDatePicker = new DatePicker();

	    if (editableObject instanceof Transaction) {
	        Transaction transaction = (Transaction) editableObject;

	        accountComboBox.setValue(transaction.getAccount());
	        transTypeComboBox.setValue(transaction.getTransactionType());
	        transDatePicker.setValue(transaction.getTransactionDate());

	        fieldMapping.put("Account", accountComboBox);
	        fieldMapping.put("Transaction Type", transTypeComboBox);
	        fieldMapping.put("Transaction Date", transDatePicker);

	        Label transDescLabel = new Label("Description:");
	        TextField transDescField = new TextField(transaction.getDescription());
	        fieldMapping.put("Description", transDescField);

	        Label paymentLabel = new Label("Payment Amount:");
	        TextField paymentField = new TextField(String.valueOf(transaction.getPaymentAmount()));
	        fieldMapping.put("Payment Amount", paymentField);

	        Label depositLabel = new Label("Deposit Amount:");
	        TextField depositField = new TextField(String.valueOf(transaction.getDepositAmount()));
	        fieldMapping.put("Deposit Amount", depositField);

	        editLayout.getChildren().addAll(
	                accountLabel, accountComboBox,
	                transTypeLabel, transTypeComboBox,
	                transDateLabel, transDatePicker,
	                transDescLabel, transDescField,
	                paymentLabel, paymentField,
	                depositLabel, depositField
	        );
	    } else if (editableObject instanceof ScheduledTransaction) {
	        ScheduledTransaction scheduledTransaction = (ScheduledTransaction) editableObject;

	        accountComboBox.setValue(scheduledTransaction.getAccount());
	        transTypeComboBox.setValue(scheduledTransaction.getTransactionType());

	        fieldMapping.put("Account", accountComboBox);
	        fieldMapping.put("Transaction Type", transTypeComboBox);

	        Label nameLabel = new Label("Name:");
	        TextField nameField = new TextField(scheduledTransaction.getName());
	        fieldMapping.put("Name", nameField);

	        Label dueDateLabel = new Label("Due Date:");
	        TextField dueDateField = new TextField(String.valueOf(scheduledTransaction.getDueDate()));
	        fieldMapping.put("Due Date", dueDateField);

	        Label paymentLabel = new Label("Payment Amount:");
	        TextField paymentField = new TextField(String.valueOf(scheduledTransaction.getPayAmount()));
	        fieldMapping.put("Payment Amount", paymentField);

	        editLayout.getChildren().addAll(
	                accountLabel, accountComboBox,
	                transTypeLabel, transTypeComboBox,
	                nameLabel, nameField,
	                dueDateLabel, dueDateField,
	                paymentLabel, paymentField
	        );
	    }

	    Button saveButton = new Button("Save");
	    Button backButton = new Button("Back");
	    HBox buttonBox = new HBox(10, saveButton, backButton);
	    buttonBox.setAlignment(Pos.CENTER);

	    editLayout.getChildren().add(buttonBox);

	    saveButton.setOnAction(e -> {
	        try {
	            if (editableObject instanceof Transaction) {
	                Transaction originalTransaction = (Transaction) editableObject;
	                Transaction updatedTransaction = new Transaction(
	                        originalTransaction.getAccount(), 
	                        originalTransaction.getTransactionType(),
	                        originalTransaction.getTransactionDate(),
	                        originalTransaction.getDescription(),
	                        originalTransaction.getPaymentAmount(),
	                        originalTransaction.getDepositAmount()
	                    );
	                updatedTransaction.setAccount(((ComboBox<String>) fieldMapping.get("Account")).getValue());
	                updatedTransaction.setTransactionType(((ComboBox<String>) fieldMapping.get("Transaction Type")).getValue());
	                updatedTransaction.setTransactionDate(((DatePicker) fieldMapping.get("Transaction Date")).getValue());
	                updatedTransaction.setDescription(((TextField) fieldMapping.get("Description")).getText().trim());
	                updatedTransaction.setPaymentAmount(Double.parseDouble(((TextField) fieldMapping.get("Payment Amount")).getText().trim()));
	                updatedTransaction.setDepositAmount(Double.parseDouble(((TextField) fieldMapping.get("Deposit Amount")).getText().trim()));

	                originalTransaction.save(originalTransaction, updatedTransaction);
	                displayTransactions(primary); 
	            } else if (editableObject instanceof ScheduledTransaction) {
	            	ScheduledTransaction originalScheduledTransaction = (ScheduledTransaction) editableObject;
	                ScheduledTransaction updatedScheduledTransaction = new ScheduledTransaction(
	                		originalScheduledTransaction.getAccount(),
	                		originalScheduledTransaction.getTransactionType(),
	                		originalScheduledTransaction.getName(),
	                		originalScheduledTransaction.getDueDate(),
	                		originalScheduledTransaction.getPayAmount()
	                	);
	                updatedScheduledTransaction.setAccount(((ComboBox<String>) fieldMapping.get("Account")).getValue());
	                updatedScheduledTransaction.setTransactionType(((ComboBox<String>) fieldMapping.get("Transaction Type")).getValue());
	                updatedScheduledTransaction.setName(((TextField) fieldMapping.get("Name")).getText().trim());
	                updatedScheduledTransaction.setDueDate(Integer.parseInt(((TextField) fieldMapping.get("Due Date")).getText().trim()));
	                updatedScheduledTransaction.setPayAmount(Double.parseDouble(((TextField) fieldMapping.get("Payment Amount")).getText().trim()));

	                originalScheduledTransaction.save(originalScheduledTransaction, updatedScheduledTransaction);
	            }

	            
	            Stage stage = (Stage) saveButton.getScene().getWindow();
	            stage.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    });

	    backButton.setOnAction(e -> {
	        Stage stage = (Stage) backButton.getScene().getWindow();
	        stage.close();
	        if (editableObject instanceof Transaction) {
	            displayTransactions(primary);
	        } else if (editableObject instanceof ScheduledTransaction) {
	            displayScheduledTransactions(primary);
	        }
	    });

	    Scene editScene = new Scene(editLayout, 400, 500);
	    Stage editStage = new Stage();
	    editStage.setScene(editScene);
	    editStage.setTitle("Edit " + editableObject.getClass().getSimpleName());
	    editStage.show();
	}

	private void displayAccReportPage(Stage primary) {
		BorderPane accReportPage = new BorderPane();
		ComboBox<String> accDropDown;
		VBox searchContainer = null;;

		Button back = new Button("Home");
		back.setMinSize(50, 10);

		TableView<Transaction> table = new TableView<>();

		TableColumn<Transaction, String> transTypeCol = new TableColumn<>("Transaction Type");
		transTypeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionType()));

		TableColumn<Transaction, String> dateCol = new TableColumn<>("Transaction Date");
		dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionDate().toString()));

		TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
		descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

		TableColumn<Transaction, Double> paymentCol = new TableColumn<>("Payment Amount");
		paymentCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPaymentAmount()).asObject());

		TableColumn<Transaction, Double> depositCol = new TableColumn<>("Deposit Amount");
		depositCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getDepositAmount()).asObject());

		table.getColumns().addAll(transTypeCol, dateCol, descCol, paymentCol, depositCol);
		try {
			List<Account> accounts = Account.getAllAccounts();
			List<String> accNames = new ArrayList<String>();
			for(Account a: accounts) {
				accNames.add(a.getName());
			}
			Collections.sort(accNames);
			ObservableList<String> accountList = FXCollections.observableList(accNames);
			accDropDown = new ComboBox<String>(accountList);
				
			
			List<Transaction> transactions = Transaction.getAllTransactions();
			ObservableList<Transaction> transactionList = FXCollections.observableList(transactions);
			table.setItems(transactionList);

			accDropDown.setOnAction(e ->{
				List<Transaction> filterTransactions = new ArrayList<Transaction>(); 
				String searchSub = accDropDown.getValue();
				if (searchSub.isEmpty()) {
					table.setItems(transactionList);
					return;
				}
				for (Transaction tran : transactions) {
					if (tran.getAccount().toLowerCase().contains(searchSub.toLowerCase())) {
						filterTransactions.add(tran);
					}
				}

				ObservableList<Transaction> filterList = FXCollections.observableList(filterTransactions);
				table.setItems(filterList);
			});
			
			Label accChooseLabel = new Label("Choose Account:");
			searchContainer = new VBox(accChooseLabel, accDropDown);
				
			
		} catch(IOException e) {
			System.out.println("No accounts yet");
		}

		
		

		dateCol.setSortType(TableColumn.SortType.DESCENDING);
		table.getSortOrder().add(dateCol);

		accReportPage.setTop(searchContainer);
		accReportPage.setCenter(table);

		back.setOnAction(e -> backToHomePage(primary));

		VBox bottomContent = new VBox(back);
		bottomContent.setAlignment(Pos.CENTER);
		bottomContent.setPadding(new Insets(15));
		accReportPage.setBottom(bottomContent);

		Scene transactionScene = new Scene(accReportPage, 1280, 800);
		primary.setScene(transactionScene);
		primary.show();
	}
	
	private void displayTTReportPage(Stage primary) {
		
	}
	
	private TextField createTextField(VBox layout, String label, String value) {
	    Label fieldLabel = new Label(label);
	    TextField textField = new TextField(value);
	    layout.getChildren().addAll(fieldLabel, textField);
	    return textField;
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

		public static ArrayList<String> readSchedulesFromCSV(String filename) {
			ArrayList<String> schedules = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				String line;
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (!line.isEmpty()) {
						schedules.add(line);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return schedules;
		}
	}
}
