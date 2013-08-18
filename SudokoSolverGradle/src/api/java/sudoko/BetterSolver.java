package sudoko;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class BetterSolver {
	private GameBoard theGameBoard;
	private TreeMap<Integer,GameBoard> allTheGameBoards;
	private TreeMap<Integer,GameBoard> temporaryAllGameBoards;
	private TreeMap<Integer,GameBoard> shortenedMapOfGameBoards;
	private int first = 1;
	private GameBoard nonChangingBoard;
	ArrayList<Integer> listHashesForRemoval;
	
	public BetterSolver(){
		theGameBoard = new GameBoard();
		readInValues();
		allTheGameBoards = new TreeMap<Integer,GameBoard>();
		temporaryAllGameBoards = new TreeMap<Integer,GameBoard>();
		shortenedMapOfGameBoards = new TreeMap<Integer,GameBoard>();
		allTheGameBoards.put(theGameBoard.hashCode(), theGameBoard);
		listHashesForRemoval = new ArrayList<Integer>();
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
		
		fillInAllGameBoardsWithNewBoards();//TODO redo method
		first = 0;
		if(allTheGameBoards.size() == 0) break;
//		temporaryAllGameBoards = allTheGameBoards;
		for (GameBoard aGameBoard : shortenedMapOfGameBoards.values()) {
			
				runIteration(aGameBoard);
				generateNewGuessBoards(aGameBoard);
				if(aGameBoard.checkForFailedCells()  || aGameBoard.isFailed()) listHashesForRemoval.add(aGameBoard.hashCode());
				else{
					if(aGameBoard.isSolved()){
						if(aGameBoard.checkForFailedCells()){
							listHashesForRemoval.add(aGameBoard.hashCode());
						}
						else{
							printFinishedBoard(aGameBoard);
							break outer;
						}
					
				}
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
	aGameBoard.getTotal();
	System.out.print(aGameBoard.total);
	System.out.print("\n");
	System.out.print("\n");
}

private void printFinishedBoard(GameBoard aGameBoard) {
	System.out.println("Sucess!");
	printBoard(aGameBoard);
}

private void fillInAllGameBoardsWithNewBoards() {
	
	for (GameBoard aGameBoard : allTheGameBoards.values()) {
		if(aGameBoard.checkForFailedCells()  || aGameBoard.isFailed()) listHashesForRemoval.add(aGameBoard.hashCode());
	}
	
	if(!listHashesForRemoval.isEmpty()){
		for (Integer hash : listHashesForRemoval) {
		allTheGameBoards.remove(hash);
		shortenedMapOfGameBoards.remove(hash);
	}
	}
	
	if(!temporaryAllGameBoards.isEmpty()){
		
		for(GameBoard gameBoard : temporaryAllGameBoards.values()){
			allTheGameBoards.put(gameBoard.hashCode(), gameBoard);
		}
	}
	if(shortenedMapOfGameBoards.size() == 0){
		Iterator<GameBoard> shortenerIterator = allTheGameBoards.values().iterator();
	if(allTheGameBoards.size() > 200){
		for(int allGameBoardCounter = 0; allGameBoardCounter < 200; allGameBoardCounter++){
			GameBoard buffGB = shortenerIterator.next();
			shortenedMapOfGameBoards.put(buffGB.hashCode(), buffGB);
		}
	}
	
	if(allTheGameBoards.size() <= 200){
		for(int allGameBoardCounter = 0; allTheGameBoards.size() > allGameBoardCounter; allGameBoardCounter++){
			GameBoard buffGB = shortenerIterator.next();
			shortenedMapOfGameBoards.put(buffGB.hashCode(), buffGB);
		}
	}
	}
	
	
	
	
	System.out.println("Starting to print out all the boards....." + "\n");
	for (GameBoard gameBoard : allTheGameBoards.values()) {
		printBoard(gameBoard);
	}
	System.out.println( "Ending...." + "\n");
}




private void generateNewGuessBoards(GameBoard aGameBoard) {
	nonChangingBoard = new GameBoard();
//	nonChangingBoard.setTheBoard(aGameBoard.getTheBoard());
	copyCells(nonChangingBoard, aGameBoard);
	int hash = aGameBoard.hashCode();
	if(aGameBoard.getLowestNumberOfPossibleValues() > 1){ 
		for (int lowestCellIndex = 0; lowestCellIndex < aGameBoard.getLowestPValueCells().size(); lowestCellIndex++) {
			for (int indexOfPossibleValues = 0; indexOfPossibleValues < aGameBoard.getLowestNumberOfPossibleValues(); indexOfPossibleValues++) {
				GameBoard bufferGameBoard = new GameBoard();
//				bufferGameBoard.setTheBoard(aGameBoard.getTheBoard());
				
			nonChangingBoard.setACell(aGameBoard.getLowestPValueCells().get(lowestCellIndex).getRow(), aGameBoard.getLowestPValueCells().get(lowestCellIndex).getColumn(), aGameBoard.getLowestPValueCells().get(lowestCellIndex).getPossibleValues().get(indexOfPossibleValues));
			copyCells(bufferGameBoard,nonChangingBoard);
			temporaryAllGameBoards.put(bufferGameBoard.hashCode(), bufferGameBoard);
			}
			
		}
		listHashesForRemoval.add(hash);
		
	}

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

public TreeMap<Integer, GameBoard> getAllTheGameBoards() {
	return allTheGameBoards;
}

public void copyCells(GameBoard empty, GameBoard full){
	for (Cell cell : full.getTheBoard()) {
		empty.setACell(cell.getRow(), cell.getColumn(), cell.getActualValue());
	}
}

public void setAllTheGameBoards(TreeMap<Integer, GameBoard> allTheGameBoards) {
	this.allTheGameBoards = allTheGameBoards;
}

}


