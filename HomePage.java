package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomePage extends Application{

@Override
public void start(Stage primary) {
// TODO Auto-generated method stub
BorderPane root = new BorderPane();
Scene scene = new Scene(root,1024,600);
scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
primary.setScene(scene);
primary.show();
try {
	fdkljdfklkllkdfgklgdflkgdflkgdflk
} catch (Exception e) {
e.printStackTrace();
	}
}

}
