package conway;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <h1>Cell</h1>
 * A Cell is part of the two-dimensional automaton Conway's Game of Life.
 * It can be added to a GridOfCells and can thereby interact with other Cells.
 * Cell extends Rectangle and can be positioned, sized, recolored and more.
 * Each Cell can be 'alive' or 'dead' and represents this status through its color.
 * A black Cell is alive and a white Cell is dead.
 *
 * @author  Bertan Karacora
 * @author  Jan Sturzenhecker
 * @author  Timur Schiwe
 * @version 1.0
 * @since   2020-07-06
 * @see     GridOfCells
 * @param   alive indicates the status of the Cell in the Game of Life
 * @param   iPos indicates the row the Cell is positioned in
 * @param   jPos indicates the column the Cell is positioned in
 */
public class Cell extends Rectangle {
	private boolean alive;
	private int iPos;
	private int jPos;

	/**
	 * Creates a Cell with a given position, size and status.
	 * @param  x the row the Cell is positioned in
	 * @param  y the column the Cell is positioned in
	 * @param  size the size of the square
	 * @param  value the status of the square
	 */
	public Cell(int i, int j, int size, boolean value) {
		iPos = i;
		jPos = j;
		alive = value;
		setWidth(size);
		setHeight(size);
		setFill(Color.TRANSPARENT);
		setStroke(Color.LIGHTGREY);
		setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				e.consume();
				this.changeAlive();
			}
		});
		setOnMouseDragEntered(e -> this.changeAlive());
	}

	/**
	 * Creates a Cell with a given position. Size and status are set to default values '10' and 'false'.
	 * @param  i the row the Cell is positioned in
	 * @param  j the column the Cell is positioned in
	 */
	public Cell(int i, int j) {
		this(i,j, 10, false);
	}

	/**
	 * Getter for alive.
	 * @return the status of the Cell
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Getter for iPos.
	 * @return the rows of the Cell
	 */
	public int getIPos() {
		return iPos;
	}

	/**
	 * Getter for jPos.
	 * @return the column of the Cell
	 */
	public int getJPos() {
		return jPos;
	}

	/**
	 * Setter for alive. Changes its color accordingly.
	 * @param the status of the Cell
	 */
	public void setAlive(boolean value){
		alive = value;
		if(alive) setFill(Color.BLACK);
		else setFill(Color.TRANSPARENT);
	}

	/**
	 * Inverts alive. Changes its color accordingly.
	 */
	public void changeAlive() {
		if(alive) {
			alive = false;
			setFill(Color.TRANSPARENT);
		}
		else {
			alive = true;
			setFill(Color.BLACK);
		}
	}
}
