package conway;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import java.util.Date;
import java.util.Scanner;

/**
 * <h1>Main Window Controller</h1>
 * Controls the main window of the application. Connects buttons with its functionalities, organizes the data, manages the UI.
 *
 * @author  Bertan Karacora
 * @author  Jan Sturzenhecker
 * @author  Timur Schiwe
 * @version 1.0
 * @since   2020-07-06
 * @exception IOException
 * @param   gridOfCells an instance of GridOfCells shown in the main window
 * @param   SCALE_DELTA indicates usual scaling/zooming rate
 */
public class MainWindowController {
	private final double SCALE_DELTA = 1.1;
	private double x,y;
	private GridOfCells gridOfCells;
	private Timeline timeline;
	private Gson gson;
	private File states;
	private File rules;
	@FXML private GridPane grid;
	@FXML private ScrollPane scrollPane;
	@FXML private MenuButton menuButton;
	@FXML private MenuItem beendenButton;
	@FXML private MenuItem changeRulesButton;
	@FXML private MenuItem resetButton;
	@FXML private MenuItem loadButton;
	@FXML private MenuItem loadRulesButton;
	@FXML private MenuItem saveButton;
	@FXML private MenuItem saveRulesButton;
	@FXML private MenuItem quitButton;
	@FXML private Slider simulationSpeedSlider;
	@FXML private Label speedLabel;
	@FXML private Label stepsLabel;
	@FXML private Label cellsAliveCounterLabel;
	@FXML private Button startOrPauseButton;
	@FXML private Button stepButton;
	@FXML private Button invertRulesButton;
	@FXML private Button invertCellsButton;
	@FXML private Button randomizeButton;

	/**
	 * Initializes everything.
	 */
	public void initialize() {
		gson = new Gson();
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000), e -> step())
				);
		timeline.rateProperty().bind(new DoubleBinding() {
			{
				bind(simulationSpeedSlider.valueProperty());
			}
			@Override
			protected double computeValue() {
				return Math.pow(2, simulationSpeedSlider.valueProperty().get())-1;
			}
		});
		speedLabel.textProperty().bind(new StringBinding() {
			{
				bind(timeline.currentRateProperty());
			}
			@Override
			protected String computeValue() {
				if (timeline.currentRateProperty().get() == 0) return "Speed: not running";
				return "Speed: " + Double.toString(1/(timeline.currentRateProperty().get())*1000) + "ms/Generation";
			}
		});
		startOrPauseButton.textProperty().bind(new StringBinding() {
			{
				bind(timeline.statusProperty());
			}
			@Override
			protected String computeValue() {
				if (timeline.statusProperty().get() == Status.RUNNING) return "Stop";
				else return "Start";
			}
		});
		states = new File("states");
		rules = new File("rules");
		states.mkdir();
		rules.mkdir();
		try {
			rules.createNewFile();
			states.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Generates a new grid with the default size of 50 rows and 50 columns.
	 */
	public void makeNewGrid() {
		makeNewGrid(50, 50);
	}

	/**
	 * Generates a new grid with a given amount of rows and columns.
	 * @param rows rows of the grid
	 * @param columns columns of the grid
	 */
	public void makeNewGrid(int rows, int columns) {
		gridOfCells = new GridOfCells(rows, columns);
		fill(rows, columns);
	}

	/**
	 * Fills the gridPane with Cells.
	 * @param gridRows rows of the grid
	 * @param gridColumns columns of the grid
	 */
	public void fill(int gridRows, int gridColumns) {
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridColumns; j++) {
				grid.add(gridOfCells.getCell(i, j), j, i, 1, 1);
			}
		}
	}

	/**
	 * Getter for gridOfCells.
	 * @return the current gridOfCells
	 */
	public GridOfCells getGridOfCells() {
		return gridOfCells;
	}

	/**
	 * Makes a time step forward and shows the next generation of Cells.
	 */
	@FXML public void step() {
		gridOfCells.nextGeneration();
		stepsLabel.textProperty().setValue("Generation: " + gridOfCells.getGeneration());
	}

	/**
	 * Opens a new window, where the size of the grid can be edited.
	 * The state of the grid is lost.
	 * @exception IOException
	 */
	public void reset() throws IOException{
		timeline.stop();
		grid.getChildren().clear();
		grid.getTransforms().clear();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ResetWindow.fxml"));
		Parent load = fxmlLoader.load();
		Scene scene = new Scene(load, 500, 500);
		Stage rulesStage = new Stage();
		rulesStage.initModality(Modality.APPLICATION_MODAL);
		rulesStage.setTitle("New grid");
		rulesStage.setScene(scene);
		rulesStage.show();
	}

	/**
	 * Ends the Program.
	 */
	@FXML public void quit() {
		System.exit(0);
	}

	/**
	 * Starts the automatic simulation of new generations.
	 * If a simulation is running it is paused.
	 */
	@FXML public void startOrPause() {
		if (timeline.getStatus() == Status.RUNNING) timeline.pause();
		else timeline.play();
	}

	/**
	 * Saves to current state of the simulation into Json.
	 */
	@FXML public void save() throws IOException{
		timeline.pause();
		gridOfCells.save();
		String s = gson.toJson(gridOfCells);
		String Path = "states" + File.separator + new Date().toString().replace(':', '_');
		//create File for the state
		File state = new File(Path);
		state.createNewFile();
		//write json string into state file
		FileWriter writer = new FileWriter((Path));
		writer.write(s);
		writer.close();
	}

	/**
	 * Loads a saved state of the simulation from Json into the grid.
	 */
	@FXML public void loadSimulation() throws IOException {
		String s = "";
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("states"));
		fileChooser.setTitle("Open State File ");
		File newState = fileChooser.showOpenDialog(Main.getStage());
		if (newState == null) return;
		Scanner scanner = new Scanner(newState);
		while(scanner.hasNextLine()){
			s = s + scanner.nextLine();
		}
		scanner.close();
		gridOfCells = gson.fromJson(s, GridOfCells.class);
		grid.getChildren().clear();
		gridOfCells.fillWithStatus();
		fill(gridOfCells.getRows(), gridOfCells.getColumns());
	}

	/**
	 * Inverts all Cells.
	 */
	@FXML public void invertCells() {
		gridOfCells.invert();
	}

	/**
	 * Opens a new window, where the rules can be edited.
	 * The current state of the grid is not lost.
	 * @exception IOException
	 */
	@FXML public void changeRules() throws IOException {
		timeline.stop();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ChangeRulesWindow.fxml"));
		Parent load = fxmlLoader.load();
		Scene scene = new Scene(load, 750, 500);
		Stage rulesStage = new Stage();
		rulesStage.initModality(Modality.APPLICATION_MODAL);
		rulesStage.setTitle("Change Rules");
		rulesStage.setScene(scene);
		rulesStage.show();
	}

	/**
	 * Saves to current set of rules of the simulation into Json.
	 * @exception IOException 
	 */
	@FXML public void saveRules() throws IOException {
		timeline.pause();
		gridOfCells.save();
		String s = gson.toJson(gridOfCells.getRules());
		String Path = "rules" + File.separator + new Date().toString().replace(':', '_');
		//create File for the rules
		File rulesFile = new File(Path);
		rulesFile.createNewFile();
		//write json string into rules file
		FileWriter writer = new FileWriter((Path));
		writer.write(s);
		writer.close();
	}

	/**
	 * Loads a saved set of rules from Json into the simulation.
	 * @exception FileNotFoundException 
	 */
	@FXML public void loadRules() throws FileNotFoundException {
		String s = "";
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("rules"));
		fileChooser.setTitle("Open Rules File ");
		File newRules = fileChooser.showOpenDialog(Main.getStage());
		if (newRules == null) return;
		Scanner scanner = new Scanner(newRules);
		while(scanner.hasNextLine()){
			s = s + scanner.nextLine();
		}
		scanner.close();
		gridOfCells.setRules(gson.fromJson(s, Rules.class));
	}

	/**
	 * Inverts the rules.
	 */
	@FXML public void invertRules() {
		gridOfCells.invertRules();
	}

	/**
	 * Randomizes the grid.
	 */
	@FXML public void randomize() {
		gridOfCells.randomRepopulate();
	}

	/**
	 * Method for zooming in and out.
	 */
	@FXML public void zoom(ScrollEvent scrollEvent) {
		Scale scale = new Scale();
		if (scrollEvent.getDeltaY()==0) return;
		double scaleFactor = (scrollEvent.getDeltaY() > 0) ? SCALE_DELTA : 1/SCALE_DELTA;

		//Setting the dimensions for the transformation
		scale.setX(scaleFactor);
		scale.setY(scaleFactor);

		//Setting the pivot point for the transformation (The object pivot is the position about which objects are scaled and rotated)
		scale.setPivotX(scrollEvent.getX());
		scale.setPivotY(scrollEvent.getY());

		grid.getTransforms().addAll(scale);
		scrollEvent.consume();
	}

	/**
	 * Method for moving around the pane with the mouse dragged.
	 */
	@FXML public void onMouseDragged(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			event.consume();
			grid.setTranslateX(event.getSceneX() + x);
			grid.setTranslateY(event.getSceneY() + y);
		}
	}

	/**
	 * Starts a full press-drag-release gesture. Triggers drag event in Cells.
	 */
	@FXML public void gridDragDetected(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			event.consume();
			grid.startFullDrag();
		}
	}

	/**
	 * Saves the position of the cursor if secondary mouse-button is pressed.
	 */
	@FXML public void mousePressed(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			x = grid.getTranslateX() - event.getSceneX();
			y = grid.getTranslateY() - event.getSceneY();
			scrollPane.setCursor(Cursor.MOVE);
		}
	}

	/**
	 * Resets cursor to default when drag is finished.
	 */
	@FXML public void mouseReleased(MouseEvent event) {
		event.consume();
		scrollPane.setCursor(Cursor.DEFAULT);
	}
}

