package application;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.io.FileNotFoundException;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class HomePage extends Application{
    private BorderPane pages = new BorderPane();
    private Button homeBtn = new Button("Create an account");
    private Button viewAccountsBtn = new Button("View Accounts");
    private Text title = new Text("PennyPal");

    @Override
    public void start(Stage primary) {
        try {
            Text welcome = new Text("Welcome to");

            title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
            welcome.setStyle("-fx-font-size: 25px;");
            homeBtn.setMinSize(150, 35);
            viewAccountsBtn.setManaged(150, 35);

            VBox centerContent = new VBox(homeBtn, viewAccountsBtn);
            centerContent.setAlignment(Pos.CENTER);
            centerContent.setPadding(new Insets(-100, 0, 0, 0));

            VBox topContent = new VBox(10, welcome, title);
            topContent.setAlignment(Pos.TOP_CENTER);
            topContent.setPadding(new Insets(150, 0, 0, 0));

            pages.setTop(topContent);
            pages.setCenter(centerContent);

            homeBtn.setOnAction(e -> openAccountPage(primary));
            viewAccountsBtn.setOnAction(e -> displayAccounts(primary));

            homeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> openAccountPage(primary));

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
        TableColumn<Account, String> nameCol = new TableView<>("Account Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStartingProperty(data.getValue().getName()));
        TableColumn<Account, String> nameCol = new TableView<>("Balance");
        balanceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStartingProperty(data.getValue().getBalance()));
        TableColumn<Account, String> nameCol = new TableView<>("Opening Date");
        dataCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStartingProperty(data.getValue().getOpenDate()));

        table.getColumns().addAll(nameCol, balanceCol, dateCol);

        try{
            List<Account> accounts = Account.getSortedAccountsByDate();
            ObservableList<Account> accountList = FXCollections.observableList(accounts);
            table.setItems(accountList);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

        BorderPane accountPage = new BorderPane();
        accountPage.setCenter(table);

        Button back = new Button("Button");
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
        TextField accBal = new TextField();
        TextField accDate = new TextField();
        Label nameLabel = new Label("Account name:");
        Label date = new Label("Opening date:");
        Label balance = new Label("Opening balance:");
        Button back = new Button("Home");
        Button create = new Button("Create");
        create.setMinSize(100, 30);

        nameLabel.setStyle("-fx-font-size: 15;");
        date.setStyle("-fx-font-size: 15;");
        balance.setStyle("-fx-font-size: 15;");
        createAccount.setStyle("-fx-font-size: 30; -fx-font-weight: bold;");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        ap.add(accName, 0, 1);
        ap.add(accBal, 1, 1);
        ap.add(accDate, 2, 1);
        ap.add(nameLabel, 0, 0);
        ap.add(date, 2, 0);
        ap.add(balance, 1, 0);

        ap.setHgap(250);
        ap.setVgap(5);
        ap.setAlignment(Pos.BOTTOM_CENTER);
        ap.setPadding(new Insets(0, 70, 70, 0));

        VBox homeButtonContainer = new VBox(back);
        homeButtonContainer.setPadding(new Insets(0, 0, 0, 20));

        VBox centerContent = new VBox(175, title, createAccount);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(194, 0, 0, 0));

        VBox createButtonContainer = new VBox(create);
        createButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
        createButtonContainer.setPadding(new Insets(0, 0, 100, 0));

        pages.setLeft(homeButtonContainer);
        pages.setTop(centerContent);
        pages.setCenter(ap);
        pages.setBottom(createButtonContainer);

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> backToHomePage(primary));

        //Gathers data from the 3 textFields, creates a new account with that data and stores it in Accounts.csv
        create.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            String name = accName.getText();
            Double balD = Double.parseDouble(accBal.getText());
            double bal = Double.valueOf(balD);
            String openDate = accDate.getText();

            Account newAcc  = new Account(name, bal, openDate);
            try {
                Account.storeData(newAcc);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        primary.show();

    }

    private void backToHomePage(Stage primary) {
        HomePage hp = new HomePage();
        hp.start(primary);
    }

}
