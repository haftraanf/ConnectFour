package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controllers {
	@FXML
	private TextField nameOne;
	@FXML
	private TextField nameTwo;
	@FXML
	private Label nameLabel;
	@FXML
	private Button submitButton;
	
	private static String playerOneName;
	private static String playerTwoName;
	
	private static final int CIRCLE_RADIUS = 40;
	private static final int NUM_ROWS = 6;
	private static final int NUM_COLS = 7;
	
	private boolean PlayerOneMove = true;
	private boolean placable = true;
	
	private Disk[][] board = new Disk[NUM_COLS][NUM_ROWS];//For structural changes
	private Pane diskContainer = new Pane();
		
	public void submit(ActionEvent event) throws IOException {
		playerOneName = nameOne.getText();
		playerTwoName = nameTwo.getText();

		if ((playerOneName.isEmpty() && playerTwoName.isEmpty()) || playerOneName.isEmpty() || playerTwoName.isEmpty()) {
		    nameLabel.setText("Please enter the name(s)!");
		    nameLabel.setStyle("-fx-font-size: 20px;-fx-font-family:\"Arial Black\";-fx-alignment: center;");
		} 
		else {
			Stage loginStage = (Stage) submitButton.getScene().getWindow(); // Get the login stage
		    loginStage.close(); // Close the login stage
		    Stage stage = new Stage();
		    Scene gridScene = new Scene(displayGrid());
		    stage.setScene(gridScene);
		    stage.setResizable(false);
			stage.setTitle("Connect Four");
		    stage.show();
		    
		    stage.setOnCloseRequest(e -> {
		    	event.consume();
		    	logout(stage);
		    });
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
	
	private Shape createGrid() {
		Shape boardLayout = new Rectangle((NUM_COLS + 1) * (CIRCLE_RADIUS * 2), (NUM_ROWS + 1) * (CIRCLE_RADIUS * 2));
		
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				Circle circle = new Circle(CIRCLE_RADIUS);
				circle.setCenterX(CIRCLE_RADIUS); //move it to the top left by a distance of the radius
				circle.setCenterY(CIRCLE_RADIUS); //move it to the top left by a distance of the radius
				circle.setTranslateX(col * (CIRCLE_RADIUS * 2 + 6) + (CIRCLE_RADIUS / 2) ); // add 6 to add space btw the circles 
				circle.setTranslateY(row * (CIRCLE_RADIUS * 2 + 6) + (CIRCLE_RADIUS / 2) ); // add 6 to add space btw the circles 
				boardLayout = Shape.subtract(boardLayout, circle); //circles aren't objects since we subtracting from the rectangle shape
			}
		}
		
		// Add lighting to make the grid looks more 3D
		// Use the exact code from javafx.scene.effect.Light.Distant
		Light.Distant light = new Light.Distant();
		light.setAzimuth(45);
		light.setElevation(35);
		
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(5);
		
		boardLayout.setFill(Color.valueOf("#4A90E2"));
		boardLayout.setEffect(lighting);
		
		return boardLayout;
	}
	
	private Parent displayGrid() {
		Group root = new Group();
	    Shape boardLayout = createGrid();
        root.getChildren().add(diskContainer);
        root.getChildren().add(boardLayout);
        root.getChildren().addAll(selectableColumns());
        
        return root;
    }
	
	private List<Rectangle> selectableColumns() {
        List<Rectangle> columnsList = new ArrayList<>();

        for (int col = 0; col < NUM_COLS; col++) {
            Rectangle animation = new Rectangle(CIRCLE_RADIUS * 2, (NUM_ROWS + 1) * (CIRCLE_RADIUS * 2));
            animation.setTranslateX(col * (CIRCLE_RADIUS * 2 + 6) + CIRCLE_RADIUS / 2);
            animation.setFill(Color.TRANSPARENT); // by default the animation rect will be transparent

            animation.setOnMouseEntered(event -> animation.setFill(Color.valueOf("#eeeeee66"))); //when hover w mouse, color is replaced
            animation.setOnMouseExited(event -> animation.setFill(Color.TRANSPARENT)); //when the mouse is not hovered, color is replaced

            final int column = col;
            animation.setOnMouseClicked(event -> {
            	if (placable) {
            		placable = false;
            		dropDisk(new Disk(PlayerOneMove), column);
            	}
            });
            columnsList.add(animation);
        }

        return columnsList;
    }
	
	private static class Disk extends Circle {
		private final boolean PlayerOneDisk;
		public Disk(boolean PlayerOneDisk) {
            super(CIRCLE_RADIUS, PlayerOneDisk ? Color.valueOf("#FF4081") : Color.valueOf("#FFD700"));
			this.PlayerOneDisk = PlayerOneDisk;
			setCenterX(CIRCLE_RADIUS);
			setCenterY(CIRCLE_RADIUS);
		}
	}
	
	private Disk detectDisk(int col, int row) {
        if (row < 0 || row >= NUM_ROWS || col < 0 || col >= NUM_COLS) return null;// return nothing when row or column index is invalid
        return board[col][row]; //return the disk at this position
    }
	
	private void dropDisk(Disk disk, int col) {
		int row = NUM_ROWS - 1;
				
		while (row >= 0) { 
			if (detectDisk(col, row) == null) break; // check if there is a disk already placed in that spot, if yes then break
			row--;
		}
				
		if (row < 0) return; // fail to find a proper row that is empty, can't insert

		board[col][row] = disk; //update structure
        diskContainer.getChildren().add(disk); //update gui
        disk.setTranslateX(col * (CIRCLE_RADIUS * 2 + 6) + CIRCLE_RADIUS / 2);

        final int curr_row = row;
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.6), disk);
        animation.setToY(row * (CIRCLE_RADIUS * 2 + 6) + CIRCLE_RADIUS / 2);
        animation.setOnFinished(event -> {
        	placable = true;
            if (containsWin(col, curr_row)) gameFinished();
            PlayerOneMove = !PlayerOneMove;
        });
        animation.play();
    }

	private boolean validate(List<Point2D> coordinates) {
        int aligned = 0;

        for (Point2D coordinate : coordinates) {
            int column = (int) coordinate.getX();
            int row = (int) coordinate.getY();

            Disk disk = detectDisk(column, row); //retrieving a disc at a specific spot of row and column
            
            if (disk != null && disk.PlayerOneDisk == PlayerOneMove) { //check the most recently added disk by the current player
            	aligned++;
                if (aligned == 4) return true;
            } 
            else aligned = 0;
        }
        return false;
    }
	
	private boolean containsWin(int col, int row) {
        List<Point2D> vertical = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(col, r))
                .collect(Collectors.toList());

        List<Point2D> horizontal = IntStream.rangeClosed(col - 3, col + 3)
                .mapToObj(column -> new Point2D(column, row))
                .collect(Collectors.toList());

        Point2D topLeft = new Point2D(col - 3, row - 3); //from top left to bottom right
        List<Point2D> downwardDiagonal = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> topLeft.add(i, i))
                .collect(Collectors.toList());

        Point2D botLeft = new Point2D(col - 3, row + 3); //from bottom left to top right
        List<Point2D> upwardDiagonal = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> botLeft.add(i, -i))
                .collect(Collectors.toList());

        return validate(vertical) || validate(horizontal) || validate(downwardDiagonal) || validate(upwardDiagonal);
    }
	

    private void gameFinished() {
    	String champion = PlayerOneMove? playerOneName : playerTwoName;
		System.out.println("Winner is: " + champion);

		Alert winningAlert = new Alert(Alert.AlertType.INFORMATION);
		winningAlert.setTitle("Connect Four");
		winningAlert.setHeaderText(champion + " has won the game. Congrats " + champion + "!!!");
		winningAlert.setContentText("Do youse wanna play again?");
		ButtonType yes = new ButtonType("Yessir");
		ButtonType no = new ButtonType("Noo wayy");
		winningAlert.getButtonTypes().setAll(yes,no);
		
		//After animation ends and illegal state exception are resolved
		Platform.runLater(()->{
			Optional<ButtonType> clicked = winningAlert.showAndWait();
			if (clicked.isPresent() && clicked.get() == yes){
				resetGame();
			}
			else {
				Platform.exit();
				System.exit(0);
			}
		});
		
    }

	private void resetGame() {
		diskContainer.getChildren().clear(); //Get rid of all disc from pane
		for (int row = 0; row <board.length ; row++) {
			for (int column = 0; column < board[row].length; column++) {
				board[row][column] = null; //making the spots to be null
			}
		}
		PlayerOneMove = true; 
		displayGrid();	
	}
}
