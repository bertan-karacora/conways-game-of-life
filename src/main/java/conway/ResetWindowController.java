package conway;

import java.io.IOException;

import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

/**
 * <h1>Reset-Window-Controller</h1>
 * Controls the new-grid-window. Connects buttons with its functionalities, organizes the data, manages the UI.
 *
 * @author  Bertan Karacora
 * @author  Jan Sturzenhecker
 * @author  Timur Schiwe
 * @version 1.0
 * @since   2020-07-06
 * @exception IOException
 */
public class ResetWindowController {
	@FXML private Slider columnsSlider;
	@FXML private Slider rowsSlider;
	@FXML private Label sizeLabel;
	@FXML private Button applyButton;

	/**
	 * Initializes the UI.
	 */
	public void initialize() {
		sizeLabel.textProperty().bind(new StringBinding() {
			{
				bind(rowsSlider.valueProperty());
				bind(columnsSlider.valueProperty());
			}
			@Override
			protected String computeValue() {
				return "size: " + (int)rowsSlider.valueProperty().get() + "x" + (int)columnsSlider.valueProperty().get();
			}
		});
	}

	/**
	 * Creates a new grid of the size fed in by the user and closes this window.
	 * @exception IOException
	 */
	@FXML public void applySize() throws IOException {
		Main.getMainWindowController().makeNewGrid((int) rowsSlider.getValue(), (int) columnsSlider.getValue());
		Stage stage = (Stage) applyButton.getScene().getWindow();
		stage.close();
	}
}
