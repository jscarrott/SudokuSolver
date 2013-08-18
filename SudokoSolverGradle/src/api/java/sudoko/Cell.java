package sudoko;

import java.util.ArrayList;

public class Cell {
	
	private int row;
	private int column;
	private int square;
	private int actualValue;
	private ArrayList<Integer> possibleValues;
	private boolean isFilled;
	
	public Cell(int setRow, int setColumn, int setSquare){
		setRow(setRow);
		setColumn(setColumn);
		setSquare(setSquare);
		actualValue = 0;
		possibleValues = new ArrayList<>();
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return the square
	 */
	public int getSquare() {
		return square;
	}

	/**
	 * @param square the square to set
	 */
	public void setSquare(int square) {
		this.square = square;
	}

	/**
	 * @return the actualValue
	 */
	public int getActualValue() {
		return actualValue;
	}

	/**
	 * @param actualValue the actualValue to set
	 */
	public void setActualValue(int actualValue) {
		this.actualValue = actualValue;
//		this.setFilled(true);
	}

	/**
	 * @return the possibleValues
	 */
	public ArrayList<Integer> getPossibleValues() {
		return possibleValues;
	}

	/**
	 * @param possibleValues the possibleValues to set
	 */
	public void setPossibleValues(ArrayList<Integer> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public void addPossibleValue(int valueCounter) {
		possibleValues.add(valueCounter);
		
	}
	
	public void clearPossibleValues(){
		possibleValues.clear();
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}
	

}
