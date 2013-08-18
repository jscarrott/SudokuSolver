package allTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sudoko.GameBoard;

public class BoardTest {

	GameBoard testedGameBoard = new GameBoard();
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testGameBoard() {
		
		assertEquals(81, testedGameBoard.getTheBoard().size());
		assertEquals(9, testedGameBoard.getTheBoard().get(80).getSquare());
	}
	
	@Test
	public void testFindACell(){
		System.out.println(testedGameBoard.findACell(1, 1));
		assertEquals(0, testedGameBoard.findACell(1, 1));
	}
	
	@Test
	public void testSetValue(){
		testedGameBoard.setACell(1, 1, 3);
		assertEquals(3, testedGameBoard.getTheBoard().get(0).getActualValue());
	}




}
