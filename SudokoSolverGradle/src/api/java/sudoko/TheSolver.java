package sudoko;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.collections.list.SynchronizedList;

public class TheSolver {
	
	private GameBoard theGameBoard;
	private ArrayList<GameBoard> allTheGameBoards;
	private ArrayList<GameBoard> temporaryAllGameBoards;
	private int first = 1;
	private GameBoard nonChangingBoard;
	
	public TheSolver(){
		theGameBoard = new GameBoard();
		readInValues();
		allTheGameBoards = new ArrayList<>();
		temporaryAllGameBoards = new ArrayList<>();
		allTheGameBoards.add(theGameBoard);
	}

	public GameBoard getTheGameBoard() {
		return theGameBoard;
	}

	public void setTheGameBoard(GameBoard theGameBoard) {
		this.theGameBoard = theGameBoard;
	}
	
public	void fillInPossibleValues(GameBoard aGameBoard){
		for(int valueCounter = 1; valueCounter <= 9; valueCounter++){
			for( Cell cell : aGameBoard.getTheBoard()){
//				if(!cell.isFilled()){
					if(aGameBoard.checkAll(cell, valueCounter)){
				cell.addPossibleValue(valueCounter);
//			}
				}
			
		}
		}
		
	}
	
public	void fillInSolvedValues(GameBoard aGameBoard){
		for( Cell cell : aGameBoard.getTheBoard()){
			if(cell.getPossibleValues().size() == 1 && !cell.isFilled()){
				aGameBoard.setACell(cell.getRow(), cell.getColumn(), cell.getPossibleValues().get(0));
//				break; //May fix problem TODO
			}
		}
	}
	
public void theSolvingMethod(){
	outer :  while(true) {
		
		fillInAllGameBoardsWithNewBoards();
		first = 0;
		if(allTheGameBoards.size() == 0) break;
//		temporaryAllGameBoards = allTheGameBoards;
		for (GameBoard aGameBoard : allTheGameBoards) {
			
				runIteration(aGameBoard);
				generateNewGuessBoards(aGameBoard);
				if(aGameBoard.isFailed()){
					temporaryAllGameBoards.remove(aGameBoard);
				}
				if(aGameBoard.isSolved()){
					printFinishedBoard(aGameBoard);
					break outer;
				}
//				printBoard(aGameBoard);
				
			}
		/*if(temporaryAllGameBoards.size() >1){
			Iterator<GameBoard> tempIterator = temporaryAllGameBoards.iterator();
		while(tempIterator.hasNext()){
			GameBoard next = tempIterator.next();
			generateNewGuessBoards(next);
			
			if(next.isFailed()){
				tempIterator.remove();
			}
		}
		}*/
		
	}
			
}

private void printBoard(GameBoard aGameBoard) {
	for(Cell cell : aGameBoard.getTheBoard()){
		System.out.print(cell.getActualValue());
		if(cell.getColumn() == 9){
			System.out.print("\n");
		}
	}
	System.out.print("\n");
	System.out.print("\n");
}

private void printFinishedBoard(GameBoard aGameBoard) {
	System.out.println("Sucess!");
	printBoard(aGameBoard);
}

private void fillInAllGameBoardsWithNewBoards() {
	
	if(first == 0){
		allTheGameBoards.clear();
		for(GameBoard gb : temporaryAllGameBoards){
			GameBoard gameBoard = new GameBoard();
			copyCells(gameBoard, gb);
			allTheGameBoards.add(gameBoard);
//			allTheGameBoards.add(gb);
		}
//			allTheGameBoards = temporaryAllGameBoards;
	}
	if(first == 1){
		temporaryAllGameBoards.add(allTheGameBoards.get(0));
	}
	System.out.println("Starting to print out all the boards....." + "\n");
	for (GameBoard gameBoard : allTheGameBoards) {
		printBoard(gameBoard);
	}
	System.out.println( "Ending...." + "\n");
}




private void generateNewGuessBoards(GameBoard aGameBoard) {
	nonChangingBoard = new GameBoard();
//	nonChangingBoard.setTheBoard(aGameBoard.getTheBoard());
	copyCells(nonChangingBoard, aGameBoard);
	if(aGameBoard.getLowestNumberOfPossibleValues() > 1){ 
		for (int lowestCellIndex = 0; lowestCellIndex < aGameBoard.getLowestPValueCells().size(); lowestCellIndex++) {
			for (int indexOfPossibleValues = 0; indexOfPossibleValues < aGameBoard.getLowestNumberOfPossibleValues(); indexOfPossibleValues++) {
				GameBoard bufferGameBoard = new GameBoard();
//				bufferGameBoard.setTheBoard(aGameBoard.getTheBoard());
				
			nonChangingBoard.setACell(aGameBoard.getLowestPValueCells().get(lowestCellIndex).getRow(), aGameBoard.getLowestPValueCells().get(lowestCellIndex).getColumn(), aGameBoard.getLowestPValueCells().get(lowestCellIndex).getPossibleValues().get(indexOfPossibleValues));
			copyCells(bufferGameBoard,nonChangingBoard);
			temporaryAllGameBoards.add(bufferGameBoard);
			}
			
		}
		temporaryAllGameBoards.remove(aGameBoard);
	}
	else temporaryAllGameBoards.add(aGameBoard);
}


	

public GameBoard runIteration(GameBoard aGameBoard){
	aGameBoard.clearPossibleValues();
	fillInPossibleValues(aGameBoard);
	aGameBoard.findLowestNumberOfPossibleValues();
	if(aGameBoard.getLowestNumberOfPossibleValues() == 1) {
		fillInSolvedValues(aGameBoard);
	}
	aGameBoard.checkFinished();
	return aGameBoard;
}

	

public boolean checkSomeSolvedValues(){
	if(theGameBoard.isSomeSolvedValues()) return true;
	else return false;
}
public void readInValues(){
	SudokoReader reader = new SudokoReader();
	ArrayList<Integer> numbers = new ArrayList<>();;
	try {
		numbers  = reader.readInSudoko();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	for(int counter = 0; counter <= 80; counter++){
		theGameBoard.getTheBoard().get(counter).setActualValue(numbers.get(counter));
		if(theGameBoard.getTheBoard().get(counter).getActualValue() != 0) {
			theGameBoard.getTheBoard().get(counter).setFilled(true);
		}
	}
}

public ArrayList<GameBoard> getAllTheGameBoards() {
	return allTheGameBoards;
}

public void copyCells(GameBoard empty, GameBoard full){
	for (Cell cell : full.getTheBoard()) {
		empty.setACell(cell.getRow(), cell.getColumn(), cell.getActualValue());
	}
}

public void setAllTheGameBoards(ArrayList<GameBoard> allTheGameBoards) {
	this.allTheGameBoards = allTheGameBoards;
}

}
