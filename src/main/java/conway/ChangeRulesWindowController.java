package conway;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;

/**
 * <h1>Change-Rules-Window-Controller</h1>
 * Controls the change-rules-window. Connects buttons with its functionalities, organizes the data, manages the UI.
 *
 * @author  Bertan Karacora
 * @author  Jan Sturzenhecker
 * @author  Timur Schiwe
 * @version 1.0
 * @since   2020-07-06
 * @exception IOException
 */
public class ChangeRulesWindowController {
	@FXML private CheckBox checkBoxSurviveAt0;
	@FXML private CheckBox checkBoxSurviveAt1;
	@FXML private CheckBox checkBoxSurviveAt2;
	@FXML private CheckBox checkBoxSurviveAt3;
	@FXML private CheckBox checkBoxSurviveAt4;
	@FXML private CheckBox checkBoxSurviveAt5;
	@FXML private CheckBox checkBoxSurviveAt6;
	@FXML private CheckBox checkBoxSurviveAt7;
	@FXML private CheckBox checkBoxSurviveAt8;
	@FXML private CheckBox checkBoxBornAt0;
	@FXML private CheckBox checkBoxBornAt1;
	@FXML private CheckBox checkBoxBornAt2;
	@FXML private CheckBox checkBoxBornAt3;
	@FXML private CheckBox checkBoxBornAt4;
	@FXML private CheckBox checkBoxBornAt5;
	@FXML private CheckBox checkBoxBornAt6;
	@FXML private CheckBox checkBoxBornAt7;
	@FXML private CheckBox checkBoxBornAt8;
	@FXML private Button applyRulesButton;

	/**
	 * Creates new rules with the values of the Checkboxes and feeds them into the grid. Closes this window.
	 * @exception IOException
	 */
	@FXML public void applyRules() throws IOException {
		boolean[] newSurvivesAt = new boolean[]
				{checkBoxSurviveAt0.selectedProperty().get(), 
						checkBoxSurviveAt1.selectedProperty().get(),
						checkBoxSurviveAt2.selectedProperty().get(),
						checkBoxSurviveAt3.selectedProperty().get(),
						checkBoxSurviveAt4.selectedProperty().get(),
						checkBoxSurviveAt5.selectedProperty().get(),
						checkBoxSurviveAt6.selectedProperty().get(),
						checkBoxSurviveAt7.selectedProperty().get(),
						checkBoxSurviveAt8.selectedProperty().get()};
		boolean[] newBornAt = new boolean[]
				{checkBoxBornAt0.selectedProperty().get(), 
						checkBoxBornAt1.selectedProperty().get(),
						checkBoxBornAt2.selectedProperty().get(),
						checkBoxBornAt3.selectedProperty().get(), 
						checkBoxBornAt4.selectedProperty().get(),
						checkBoxBornAt5.selectedProperty().get(),
						checkBoxBornAt6.selectedProperty().get(),
						checkBoxBornAt7.selectedProperty().get(),
						checkBoxBornAt8.selectedProperty().get()};
		Rules newRules = new Rules(newSurvivesAt, newBornAt);
		Main.getMainWindowController().getGridOfCells().setRules(newRules);
		Stage stage = (Stage) applyRulesButton.getScene().getWindow();
		stage.close();
	}
}
