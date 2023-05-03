package conway;

import java.lang.IllegalArgumentException;

/**
 * <h1>Rules</h1>
 * An instance of the Rules class represents a set of rules applicable in an instance of GridOfCells.
 *
 * @author  Bertan Karacora
 * @author  Jan Sturzenhecker
 * @author  Timur Schiwe
 * @version 1.0
 * @since   2020-07-06
 * @see     GridOfCells
 * @exception IllegalArgumentException
 * @param   survivesAt array of booleans. Position x indicates if a cell with x neighbors survives the next generation
 * @param   bornAt array of booleans. Position x indicates if a cell with x neighbors is born at the next generation
 */
public class Rules {
	private boolean[] survivesAt;
	private boolean[] bornAt;

	/**
	 * Creates an instance of Rules with a given array of booleans sA and a given array bA
	 * @param  sA the boolean array for surviving rules
	 * @param  bA the boolean array for birth rules
	 * @exception IllegalArgumentException is thrown if sA or bA has a size unequal 9.
	 */
	public Rules(boolean[] sA, boolean[] bA) {
		if (sA.length != 9 || bA.length != 9) throw new IllegalArgumentException();
		survivesAt = sA;
		bornAt = bA;
	}

	/**
	 * Getter for the array of boolean values 'survivesAt'.
	 * @return for each position the boolean value of Rules instance
	 * 		 that indicates if a Cell with the corresponding amount of living neighbors stays alive in the next generation
	 */
	public boolean[] getSurvivesAt() {
		return survivesAt;
	}

	/**
	 * Getter for the array of boolean values 'survivesAt'.
	 * @return for each position the boolean value of Rules instance
	 * 		 that indicates if a Cell with the corresponding amount of living neighbors is born in the next generation
	 */
	public boolean[] getBornAt() {
		return bornAt;
	}

	/**
	 * Inverts all boolean values of survivesAt and bornAt.
	 */
	public void invert() {
		for (int i = 0; i < survivesAt.length; i++) {
			survivesAt[i] = !survivesAt[i];
			bornAt[i] = !bornAt[i];
		}
	}
}
