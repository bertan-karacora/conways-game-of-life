package conway;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

/**
 * <h1>Main Class</h1>
 * Launches the Application. Loads all FXML files and provides an api for its controllers.
 * The Game Of Life program visualizes a representation of Conway's famous Game Of Life automaton.
 *
 * @author  Bertan Karacora
 * @author  Jan Sturzenhecker
 * @author  Timur Schiwe
 * @version 1.0
 * @since   2020-07-06
 * @exception IOException
 * @param   primStage the primary stage of the application
 * @param   mainWindowController the controller for the main window of the application
 * @param   resetWindowController the controller for the reset window
 * @param   changeRulesWindowController the controller for the change rules window
 */
public class Main extends Application {
	private static Stage primStage;
	private static MainWindowController mainWindowController;
	private static ResetWindowController resetWindowController;
	private static ChangeRulesWindowController changeRulesWindowController;

	/**
	 * Launches the application.
	 */
	@Override
	public void start(Stage primaryStage) {
		primStage = primaryStage;
		primStage.setTitle("Conway's Game of Life");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/ResetWindow.fxml"));
		FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/ChangeRulesWindow.fxml"));
		try {
			Parent root = loader.load();
			loader2.load();
			loader3.load();
			mainWindowController = loader.getController();
			resetWindowController = loader2.getController();
			changeRulesWindowController = loader3.getController();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
			mainWindowController.makeNewGrid();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter for mainWindowController.
	 * @return the controller of the main window
	 */
	public static Stage getStage() {
		return primStage;
	}
	/**
	 * Getter for mainWindowController.
	 * @return the controller of the main window
	 */
	public static MainWindowController getMainWindowController() {
		return mainWindowController;
	}

	/**
	 * Getter for resetWindowController.
	 * @return the controller of the reset window
	 */
	public static ResetWindowController getResetWindowController() {
		return resetWindowController;
	}

	/**
	 * Getter for changeRulesWindowController.
	 * @return the controller of the change rules window
	 */
	public static ChangeRulesWindowController getChangeRulesWindowController() {
		return changeRulesWindowController;
	}

	/**
	 * If necessary, runs the application if main() is called.
	 */
	public static void main(String[] args) {
		launch();
	}
}
