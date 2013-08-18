/**
 * 
 */
package sudoko;

import java.util.ArrayList;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Home
 *
 */
public class GameBoard {
	
	private ArrayList<Cell> theBoard;
	private boolean someSolvedValues;
	private boolean solved;
	private int lowestNumberOfPossibleValues;
	private boolean failed;
	private ArrayList<Cell> lowestPValueCells;
	int total;
	private int cTotal;

	public GameBoard(){
		theBoard = new ArrayList<>();
		setupTheBoard();
		lowestPValueCells = new ArrayList<>();
		
	}
	private void setupTheBoard() {
		for (int rowCounter = 1; rowCounter <= 9; rowCounter++) {
			for (int columnCounter = 1; columnCounter <= 9; columnCounter++) {
				if (rowCounter <= 3 && columnCounter <= 3 && rowCounter <=3 && columnCounter <= 3) {
					theBoard.add(new Cell(rowCounter, columnCounter, 1));
				} else if (rowCounter >= 3 && columnCounter <= 3 && rowCounter <=6 && columnCounter <= 3) {
					theBoard.add(new Cell(rowCounter, columnCounter, 2));
				} else if (rowCounter <= 3 && columnCounter >= 3 && rowCounter <=3 && columnCounter <= 6) {
					theBoard.add(new Cell(rowCounter, columnCounter, 3));
				} else if (rowCounter >= 3 && columnCounter >= 3 && rowCounter <=6 && columnCounter <= 6) {
					theBoard.add(new Cell(rowCounter, columnCounter, 4));
				} else if (rowCounter >= 6 && columnCounter <= 3 && rowCounter <=9 && columnCounter <= 3) {
					theBoard.add(new Cell(rowCounter, columnCounter, 5));
				} else if (rowCounter <= 3 && columnCounter >= 6 && rowCounter <=3 && columnCounter <= 9) {
					theBoard.add(new Cell(rowCounter, columnCounter, 6));
				} else if (rowCounter >= 3 && columnCounter >= 6 && rowCounter <=6 && columnCounter <= 9) {
					theBoard.add(new Cell(rowCounter, columnCounter, 7));
				} else if (rowCounter >= 6 && columnCounter >= 3 && rowCounter <=9 && columnCounter <= 6) {
					theBoard.add(new Cell(rowCounter, columnCounter, 8));
				} else if (rowCounter >= 6 && columnCounter >= 6 && rowCounter <=9 && columnCounter <= 9) {
					theBoard.add(new Cell(rowCounter, columnCounter, 9));
				}
			}
		}
		
	}
	public ArrayList<Cell> getTheBoard() {
		return theBoard;
	}

	public void setTheBoard(ArrayList<Cell> theBoard) {
		this.theBoard = theBoard;
	}
	
	/**
	 * @return the someSolvedValues
	 */
	public boolean isSomeSolvedValues() {
		return someSolvedValues;
	}
	/**
	 * @param someSolvedValues the someSolvedValues to set
	 */
	public void setSomeSolvedValues(boolean someSolvedValues) {
		this.someSolvedValues = someSolvedValues;
	}
	public void setACell(int row, int column, int value){
		int index = findACell(row, column);
		theBoard.get(index).setActualValue(value);
		if(value != 0) theBoard.get(index).setFilled(true);
	}
	
	public int findACell(int row, int column){
		for(Cell cell : theBoard){
			if(cell.getRow() == row && cell.getColumn() == column){
				return theBoard.indexOf(cell);
			}
		}
		return 0;
	}
	
	public int getTotal(){
		total = 0;
		for (Cell cell : theBoard) {
			total = cell.getActualValue() + total;
		}
		return total;
	}
	
	public int getTotalCumulative(){
				for (Cell cell : theBoard) {
					cTotal = cell.getActualValue() + cTotal;
				}
				return cTotal;
			}
	
	public int hashCode(){
		return new HashCodeBuilder(27,13).append(theBoard).toHashCode();
	}
	
	public boolean testRow(Cell cell, int value){
		int rowNumber = cell.getRow();
		for(Cell aCell :theBoard){
			if(aCell.getRow() == rowNumber){
				if(aCell.getActualValue() == value &&( aCell.getColumn() != cell.getColumn() )){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean testColumn(Cell cell, int value){
		int columnNumber = cell.getColumn();
		for(Cell aCell :theBoard){
			if(aCell.getColumn() == columnNumber){
				if(aCell.getActualValue() == value && (aCell.getRow() != cell.getRow())){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean testSquare(Cell cell, int value){
		int squareNumber = cell.getSquare();
		for(Cell aCell :theBoard){
			if(aCell.getSquare() == squareNumber){
				if(aCell.getActualValue() == value &&( aCell.getColumn() != cell.getColumn() )&& (aCell.getRow() != cell.getRow())){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean checkAll(Cell cell, int value){
		if(testRow(cell, value) && testColumn(cell, value) && testSquare(cell, value)){

			return true;
		}
		return false;
	}
	
	public boolean checkForFailedCells(){
		
		if(getTotal() > 405){
			return true;
		}
		
		if(getTotalCumulative() > 4000){
			return false;
		}
		
		for(int value = 1; value < 9; value++){
			for (Cell cell : theBoard) {
				if(!testSquareIncludingOriginalCell(cell, value) || !testColumnIncludingOriginalCell(cell, value) || !testrowIncludingOriginalCell(cell, value)){
					return false;
				}
			}
		}
		return true;
		   
	}
	
	public boolean testSquareIncludingOriginalCell(Cell cell, int value){
		int squareNumber = cell.getSquare();
		int ifFilledOneAppearanceIsAcceptable = 0;
		int appearenceCounter = 0;
		if(cell.isFilled()) ifFilledOneAppearanceIsAcceptable = 1;
		for(Cell aCell :theBoard){
			if(aCell.getSquare() == squareNumber){
				if(aCell.getActualValue() == value ){
					appearenceCounter++;
				}
			}
		}
		if(ifFilledOneAppearanceIsAcceptable == appearenceCounter) return true;
		else return false;
	}
	
	
	public boolean testrowIncludingOriginalCell(Cell cell, int value){
		int rowNumber = cell.getRow();
		int ifFilledOneAppearanceIsAcceptable = 0;
		int appearenceCounter = 0;
		if(cell.isFilled()) ifFilledOneAppearanceIsAcceptable = 1;
		for(Cell aCell :theBoard){
			if(aCell.getRow() == rowNumber){
				if(aCell.getActualValue() == value ){
					appearenceCounter++;
				}
			}
		}
		if(ifFilledOneAppearanceIsAcceptable == appearenceCounter) return true;
		else return false;
	}
	
	public boolean testColumnIncludingOriginalCell(Cell cell, int value){
		int columnNumber = cell.getColumn();
		int ifFilledOneAppearanceIsAcceptable = 0;
		int appearenceCounter = 0;
		if(cell.isFilled()) ifFilledOneAppearanceIsAcceptable = 1;
		for(Cell aCell :theBoard){
			if(aCell.getColumn() == columnNumber){
				if(aCell.getActualValue() == value ){
					appearenceCounter++;
				}
			}
		}
		if(ifFilledOneAppearanceIsAcceptable == appearenceCounter) return true;
		else return false;
	}
	
	public void clearPossibleValues(){
		for(Cell cell :theBoard){
			cell.clearPossibleValues();
		}
		someSolvedValues = false;
	}
	
	public	void checkFinished(){
		for( Cell cell : theBoard){
			if(!cell.isFilled()) return; 
		}
		setSolved(true);
	}
	
	
public static void main(String Args[]){
	GameBoard test = new GameBoard();
	ArrayList<Cell> outputBoard  = test.getTheBoard();
	int counter = 0;
	for(Cell cell : outputBoard){
		System.out.println(cell.getSquare());
		counter++;
	}
	System.out.println(counter);
}
public boolean isSolved() {
	return solved;
}
public void setSolved(boolean solved) {
	this.solved = solved;
}
public int getLowestNumberOfPossibleValues() {
	return lowestNumberOfPossibleValues;
}
public void setLowestNumberOfPossibleValues(int lowestNumberOfPossibleValues) {
	this.lowestNumberOfPossibleValues = lowestNumberOfPossibleValues;
}
public void findLowestNumberOfPossibleValues() {
	int possibleValuesLength = 10;
	int includingFilledpossibleValuesLength = 10;
	for (Cell cell : theBoard) {
		if(possibleValuesLength > cell.getPossibleValues().size()&& !cell.isFilled()){
			possibleValuesLength = cell.getPossibleValues().size();
		}
	}
	for (Cell cell : theBoard) {
		if(includingFilledpossibleValuesLength > cell.getPossibleValues().size()){
			includingFilledpossibleValuesLength = cell.getPossibleValues().size();

			
		}
	}
	if(includingFilledpossibleValuesLength == 0){
		setFailed(true);
	}
	 lowestNumberOfPossibleValues = possibleValuesLength;
	generateLowestPValueCell();
}

private void generateLowestPValueCell(){
	lowestPValueCells.clear();
	for (Cell cell : theBoard) {
		if(cell.getPossibleValues().size() == getLowestNumberOfPossibleValues()){
			getLowestPValueCells().add(cell);
		}
	}
}
public boolean isFailed() {
	return failed;
}
public void setFailed(boolean failed) {
	this.failed = failed;
}
public ArrayList<Cell> getLowestPValueCells() {
	return lowestPValueCells;
}
public void setLowestPValueCells(ArrayList<Cell> lowestPValueCells) {
	this.lowestPValueCells = lowestPValueCells;
}
}
