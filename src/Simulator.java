import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

public class Simulator extends Application{
	
	private static final int INITIAL_BET = 10;
	private static final double START_BET_MULTIPLIER = 1.0;
	private static final int INIT_NUM_OF_PLAYS = 10000;
	private static final double INTERVAL_BETWEEN_PLAYS_IN_SECONDS = 0.1;
	
	
	private static int numberOfPlays = 10000;
	private static double betMultiplier = 1.0;
	private static int initialBet = 0;
	private static int totalProfit = 0;
	private static SimulatorController simulatorControl = new SimulatorController(0, 0, 0);
	
	@Override
	public void start(Stage primaryStage){
		
		var title = new Text("Cointiplay Simulator");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD,40.0));
		title.setFill(Color.RED);
		
		var grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.add(new Label("Initial Bet in coins:"), 0,0 );
		
		var initBet = new Spinner<Integer>(10,Integer.MAX_VALUE, INITIAL_BET, 1);
		initBet.setEditable(true);
		initBet.getEditor().textProperty().addListener((observable, oldValue, newValue)->{
			try {
				Integer.parseInt(newValue);
				if (Integer.parseInt(newValue) < ((IntegerSpinnerValueFactory) initBet.getValueFactory()).getMin()){
					throw new NumberFormatException();
				}
				if (newValue.endsWith("f") || newValue.endsWith("d")) {
					initBet.getEditor().setText(newValue.substring(0, newValue.length()-1));
				}
			} catch (NumberFormatException e) {
				initBet.getEditor().setText(oldValue);
			}
		});
		grid.add(initBet, 1,0);
		
		grid.add(new Label("Bet multiplier: "), 0,1);
		
		var betMult = new Spinner<Double>(0.0001, Double.MAX_VALUE, START_BET_MULTIPLIER, 0.1);
		betMult.setEditable(true);
		betMult.getEditor().textProperty().addListener((observable, oldValue, newValue)->{
			try {
				Double.parseDouble(newValue);
				if (Double.parseDouble(newValue) < ((DoubleSpinnerValueFactory) betMult.getValueFactory()).getMin()){
					throw new NumberFormatException();
				}
				if (newValue.endsWith("f") || newValue.endsWith("d")) {
					betMult.getEditor().setText(newValue.substring(0, newValue.length()-1));
				}
			} catch (NumberFormatException e) {
				betMult.getEditor().setText(oldValue);
			}
		});
		grid.add(betMult, 1,1);
		
		grid.add(new Label("Number of Plays:"), 0,2);
		
		var numSim = new Spinner<Integer>(1, Integer.MAX_VALUE, INIT_NUM_OF_PLAYS, 1);
		numSim.setEditable(true);
		numSim.getEditor().textProperty().addListener((observable, oldValue, newValue)->{
			try {
				Integer.parseInt(newValue);
				if (Integer.parseInt(newValue) < ((IntegerSpinnerValueFactory) numSim.getValueFactory()).getMin()){
					throw new NumberFormatException();
				}
				if (newValue.endsWith("f") || newValue.endsWith("d")) {
					numSim.getEditor().setText(newValue.substring(0, newValue.length()-1));
				}
			} catch (NumberFormatException e) {
				numSim.getEditor().setText(oldValue);
			}
		});
		grid.add(numSim, 1,2);
		
		var button = new Button("Start Simulation");
		
		var root = new BorderPane();
		root.setTop(title);
		root.setCenter(grid);
		root.setBottom(button);
		BorderPane.setAlignment(title, Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(grid,Pos.CENTER);
		BorderPane.setAlignment(button, Pos.TOP_CENTER);
		
		
		
		button.setOnAction(e->{
			betMultiplier = betMult.getValue().doubleValue();
			numberOfPlays = numSim.getValue().intValue();
			initialBet = initBet.getValue().intValue();
			simulatorControl.setWager(initialBet);
			changeScene(primaryStage);
		});
		
		var startScene = new Scene(root, 400, 300);
		
		primaryStage.setScene(startScene);
		primaryStage.show();
	}
	
	public void changeScene(Stage currentStage){
		var title = new Text("Cointiplay Simulator");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD,40.0));
		title.setFill(Color.RED);
		
		var nextGrid = new GridPane();
		nextGrid.setAlignment(Pos.CENTER);
		nextGrid.setVgap(15.0);
		nextGrid.setHgap(10.0);
		
		nextGrid.add(new Text("Roll Result:"), 0, 0, 4, 1);
		
		var resultDisplay = new Text();
		resultDisplay.setTextAlignment(TextAlignment.CENTER);
		resultDisplay.setFont(new Font(30.0));
		
		var betDisplay = new Text();
		var playDisplay = new Text();
		var messageDisplay = new Text();
		messageDisplay.setTextAlignment(TextAlignment.CENTER);
		var profitDisplay = new Text();
		profitDisplay.setTextAlignment(TextAlignment.CENTER);
		
		nextGrid.add(resultDisplay, 0, 1, 4, 2);
		
		nextGrid.add(new Label("Current Bet: "), 0, 2, 1,1);
		nextGrid.add(betDisplay, 1,2,1,1);
		nextGrid.add(new Label("Current Play #: "), 2,2,1,1);
		nextGrid.add(playDisplay, 3, 2, 1, 1);
		
		nextGrid.add(messageDisplay, 0,3,4,1);
		
		nextGrid.add(profitDisplay, 0,4,4,1);
		
		var nextBottom = new HBox();
		nextBottom.setSpacing(20);
		nextBottom.setAlignment(Pos.CENTER);
		
		var list = nextBottom.getChildren();
		
		list.addAll(new Label("Initial Bet: "+ initialBet), new Label("Bet Multiply when lose: " + betMultiplier), new Label("Number of Plays: " + numberOfPlays));
		var root = new BorderPane();
		root.setTop(title);
		root.setCenter(nextGrid);
		root.setBottom(nextBottom);
		BorderPane.setAlignment(title, Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(nextGrid,Pos.CENTER);
		BorderPane.setAlignment(nextBottom, Pos.TOP_CENTER);
		
		var nextScene = new Scene(root,600, 600);
		
		currentStage.setScene(nextScene);
		currentStage.show();
		boolean result;
		for (int i = 0; i < numberOfPlays; i++){
			betDisplay.setText(Integer.toString(simulatorControl.getWager()));
			result = simulatorControl.rollAndReturnResult();
			resultDisplay.setText(Integer.toString(simulatorControl.getRollResult()));
			playDisplay.setText(Integer.toString(i+1));
			totalProfit += simulatorControl.getProfit();
			if (result){
				messageDisplay.setText("You win and earned " +Integer.toString(simulatorControl.getProfit()) + " coins.");
				simulatorControl.setWager(initialBet);
			} else{
				messageDisplay.setText("You lose and "+ Integer.toString(-1 * simulatorControl.getProfit()) + " coins was deducted from your account.");
				simulatorControl.setWager(Double.valueOf(simulatorControl.getWager() * betMultiplier).intValue());
			}
			profitDisplay.setText("Total Profit: " + Integer.toString(totalProfit));
			
			
			/*try {
				Thread.sleep(Double.valueOf(INTERVAL_BETWEEN_PLAYS_IN_SECONDS*1000).longValue());
			}catch (InterruptedException e){
				e.printStackTrace();
			}*/
		}
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
