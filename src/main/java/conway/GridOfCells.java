package conway;

import java.lang.IllegalArgumentException;
import java.util.Random;

/**
 * <h1>Grid Of Cells</h1>
 * An instance of the GridOfCells class is a data structure
 * that holds Cells ordered in a grid-like arrangement.
 * Therefore it wraps Cells in an array of arrays.
 * A GridOfCells provides functions for changing Cells according to Rules.
 * Each Cell interacts with its 8 surrounding neighbors.
 * <p>
 * <b>Note:</b> A Cell is basically a Rectangle, so an alternative implementation
 * reducing the GridOfCell to a GridOfBooleans separating it from visual properties would be possible,
 * but also result in separation of api's.
 *
 * @author  Bertan Karacora
 * @author  Jan Sturzenhecker
 * @author  Timur Schiwe
 * @version 1.0
 * @since   2020-07-06
 * @see     Cell
 * @see     Rules
 * @exception IllegalArgumentException
 * @param   rows indicates the amount of rows in the grid
 * @param   columns indicates the amount of columns in the grid
 * @param   generation indicates how many steps of time have passed
 * @param   rules the set of rules the Cells interact with each other to
 * @param   status is used to calculate the next generation and for saving the status of the GridOfCells to JSON
 * @param   population represented by an array of arrays that holds Cells in each row and column
 */
public class GridOfCells {
	private int rows;
	private int columns;
	private int generation;
	private Rules rules;
	private boolean[][] status;
	private transient Cell[][] population;

	/**
	 * Creates a new instance of GridOfCells at generation 0 with the given amount of rows and columns
	 * and given rules.
	 * @param  rows rows of the GridOfCells
	 * @param  columns columns of the GridOfCells
	 * @param  survivesAt the boolean array for surviving rules
	 * @param  bornAt the boolean array for birth rules
	 */
	public GridOfCells(int rows, int columns, boolean[] survivesAt, boolean[] bornAt) {
		if (rows < 1 || columns < 1) throw new IllegalArgumentException();
		this.rows = rows;
		this.columns = columns;
		generation = 0;
		rules = new Rules(survivesAt, bornAt);
		status = new boolean[rows][columns];
		fillWithStatus();
	}

	/**
	 * Instantiates Cells to fill population.
	 */
	public void fillWithStatus() {
		population = new Cell[rows][columns];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				Cell c = new Cell(i,j);
				c.setAlive(status[i][j]);
				population[i][j] = c;
			}
		}
	}

	/**
	 * Creates a new instance of GridOfCells at generation 0 with the given amount of rows and columns.
	 * Rules are set to the default original rules of Conway's Game.
	 * @param  rows rows of the GridOfCells
	 * @param  columns columns of the GridOfCells
	 */
	public GridOfCells(int rows, int columns) {
		this(rows, columns, new boolean[] {false, false, true, true, false, false, false, false, false}, new boolean[] {false, false, false, true, false, false, false, false, false});
	}

	/**
	 * Getter for rows.
	 * @return the amount of rows in the GridOfCells instance
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Getter for columns.
	 * @return the amount of columns in the GridOfCells instance
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Getter for columns.
	 * @return the amount of columns in the GridOfCells instance
	 */
	public int getGeneration() {
		return generation;
	}

	/**
	 * Getter for a Cell of the Population specified by its Position in the GridOfCells.
	 * @param  i indicates the row of the Cell
	 * @param  j indicates the column of the Cell
	 * @return the Cell at the given Position of the GridOfCells
	 */
	public Cell getCell(int i, int j) {
		if(0 <= i && i < rows && 0 <= j && j < columns) return population[i][j];
		else return null;
	}

	/**
	 * Getter for size.
	 * @return the amount of Cells of the GridOfCells calculated from its rows and columns
	 */
	public int getSize() {
		return status.length;
	}

	/**
	 * Checks if the Cell at a given Position is alive.
	 * A Cell outside of the GridOfCells (not initiated) is considered dead.
	 * @return true or false regarding the status of the Cell
	 */
	public boolean isCellAlive(int i, int j) {
		if (0 <= i && i < rows && 0 <= j && j < columns) return population[i][j].isAlive();
		else return false;
	}

	/**
	 * Getter for rules.
	 * @return the rules of the game
	 */
	public Rules getRules() {
		return rules;
	}

	/**
	 * Setter for rules.
	 * @param the rules of the game
	 */
	public void setRules(Rules newRules) {
		rules = newRules;
	}

	/**
	 * Calculates the status of each Cell of the next generation applying the rules.
	 */
	public void nextGeneration() {
		generation++;
		for (int i = 0; i < rows; i++) {     			 
			for (int j = 0; j < columns; j++) {
				if (rules.getSurvivesAt()[amountOfNeighboursAlive(i, j)]) status[i][j] = isCellAlive(i, j);
				if (rules.getBornAt()[amountOfNeighboursAlive(i, j)] && !isCellAlive(i, j)) status[i][j] = true;
				else if (!rules.getSurvivesAt()[amountOfNeighboursAlive(i, j)]) status[i][j] = false;
			}
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (status[i][j] == true) population[i][j].setAlive(true);
				else population[i][j].setAlive(false);
			}
		}
	}

	/**
	 * Checks how many Cells surrounding the Cell at the given position are alive.
	 * @return the amount of neighboring Cells that are alive
	 */
	public int amountOfNeighboursAlive(int i, int j) {
		int aliveNeighboursCounter = 0;
		for (int k = -1; k < 2; k++) {          // iterates rows around cell
			for (int l = -1; l < 2; l++) {      // iterates columns around cell
				if (isCellAlive(i + k, j + l)) aliveNeighboursCounter++;
			}
		}
		if (isCellAlive(i, j)) aliveNeighboursCounter--;
		return aliveNeighboursCounter;
	}

	/**
	 * Counts all Cells that are alive in this generation.
	 * @return the total amount of Cells alive in this generation
	 */
	public int getAliveCells(){
		int aliveCounter = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (population[i][j].isAlive()) aliveCounter++;
			}
		}
		return aliveCounter;
	}

	/**
	 * Resets all Cells of this generation to dead.
	 */
	public void reset(){
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				population[i][j].setAlive(false);
			}
		}
	}

	/**
	 * Repopulates the GridOfCells randomly (~50/50).
	 */
	public void randomRepopulate() {
		Random random = new Random();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				population[i][j].setAlive(random.nextBoolean());
			}
		}
	}

	/**
	 * Inverts the status of all Cells of this generation.
	 */
	public void invert() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				population[i][j].changeAlive();
			}
		}
	}

	/**
	 * Inverts the rules.
	 */
	public void invertRules() {
		rules.invert();
	}

	/**
	 * Copies the status of all the Cells into the boolean array status.
	 * The position of a Cell is 'encoded' in its position in the array.
	 */
	public void save() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				status[i][j] = population[i][j].isAlive();
			}
		}
	}
}
