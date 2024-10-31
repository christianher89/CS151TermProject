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
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.application.Application;
import javafx.scene.Node;
import java.text.DecimalFormat;
import java.io.IOException;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class HomePage extends Application{
    private BorderPane pages = new BorderPane();
    private Button homeBtn = new Button("Create an account");
    private Button viewAccountsBtn = new Button("View Accounts");
    private Button transactionTypeBtn = new Button("Create Transaction Type");
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

            VBox centerContent = new VBox(10, homeBtn, viewAccountsBtn, transactionTypeBtn);
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
        Button back = new Button("Home");
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
        
        VBox createButtonContainer = new VBox(create);
        createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
        createButtonContainer.setPadding(new Insets(0, 0, 100, 0));
        createButtonContainer.getChildren().add(warning);
        createButtonContainer.getChildren().add(back);
        createButtonContainer.setSpacing(25);
  
        pages.setTop(centerContent);
        pages.setCenter(ap);
        pages.setBottom(createButtonContainer);

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));
        
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
            
            Account newAcc = new Account(name, openDate, bal);
            warning.setTextFill(Color.GREEN);
            warning.setText("New account: " + newAcc.getName() + " created successfully.");
            try {
                Account.storeData(newAcc);
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
        
        createTT.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        transTypeLabel.setStyle("-fx-font-size: 15;");
        
        
        ttp.add(transTypeName, 1, 1);
        ttp.add(transTypeLabel, 1, 0);
        
        ttp.setHgap(75);
        ttp.setVgap(5);
        ttp.setAlignment(Pos.BOTTOM_CENTER);
        ttp.setPadding(new Insets(0, 70, 70, 0));
        
        VBox centerContent = new VBox(175, title, createTT);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(194, 0, 0, 0));
        
        VBox createButtonContainer = new VBox(createTTBtn);
        createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
        createButtonContainer.setPadding(new Insets(0, 0, 100, 0));
        createButtonContainer.getChildren().add(back);
        createButtonContainer.setSpacing(25);
        
        pages.setCenter(ttp);
        pages.setTop(centerContent);
        pages.setBottom(createButtonContainer);
        
        createTTBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        	
        });
        
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));
        
        primary.show();
        
    }
}