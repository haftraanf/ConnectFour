package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("LogInScene.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Connect Four");
			stage.show();
			stage.setOnCloseRequest(event -> {
				event.consume();
				logout(stage);
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logout(Stage stage) {
		Alert logoutAlert = new Alert(AlertType.CONFIRMATION);
		logoutAlert.setTitle("Logout");
		logoutAlert.setHeaderText("ARE YOUSE SURE???");
		logoutAlert.setContentText("Youse are about to miss out on the greatest game ever...");
		ButtonType yes = new ButtonType("It's the worst game ever!");
		ButtonType no = new ButtonType("Convinced :)");
		logoutAlert.getButtonTypes().setAll(yes,no);
		
		if(logoutAlert.showAndWait().get() == yes) {
			stage.close();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
