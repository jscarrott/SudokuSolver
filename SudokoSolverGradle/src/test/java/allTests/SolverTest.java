package allTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import sudoko.BetterSolver;
import sudoko.Cell;
import sudoko.TheSolver;

public class SolverTest {

	BetterSolver testSolver;
	@Before
	public void setUp() throws Exception {
	}

	 @Test
	public void test() {
		testSolver = new BetterSolver();
		//setExampleGrid(testSolver);
//		testSolver.readInValues();
		
		for(Cell cell : testSolver.getTheGameBoard().getTheBoard()){
			System.out.print(cell.getActualValue());
			if(cell.getColumn() == 9){
				System.out.print("\n");
			}
			
		}
		System.out.print("\n");
		System.out.print("\n");
		
		testSolver.theSolvingMethod();
		
		
		assertEquals(2, testSolver.getTheGameBoard().getTheBoard().get(testSolver.getTheGameBoard().findACell(2, 2)).getActualValue());
	}
	
	private void setExampleGrid(TheSolver testSolver) {
		testSolver.getTheGameBoard().setACell(2, 1, 1);
		testSolver.getTheGameBoard().setACell(6, 1, 4);
		testSolver.getTheGameBoard().setACell(9, 1, 2);
		testSolver.getTheGameBoard().setACell(1, 2, 5);
		testSolver.getTheGameBoard().setACell(6, 2, 2);
		testSolver.getTheGameBoard().setACell(8, 2, 4);
		testSolver.getTheGameBoard().setACell(4, 3, 9);
		testSolver.getTheGameBoard().setACell(5, 3, 8);
		testSolver.getTheGameBoard().setACell(7, 3, 1);
		testSolver.getTheGameBoard().setACell(3, 4, 9);
		testSolver.getTheGameBoard().setACell(4, 4, 3);
		testSolver.getTheGameBoard().setACell(7, 4, 4);
		testSolver.getTheGameBoard().setACell(9, 4, 6);
		testSolver.getTheGameBoard().setACell(3, 5, 6);
		testSolver.getTheGameBoard().setACell(7, 5, 7);
		testSolver.getTheGameBoard().setACell(8, 5, 8);
		testSolver.getTheGameBoard().setACell(1, 6, 1);
		testSolver.getTheGameBoard().setACell(2, 6, 3);
		testSolver.getTheGameBoard().setACell(8, 6, 9);
		testSolver.getTheGameBoard().setACell(3, 7, 4);
		testSolver.getTheGameBoard().setACell(4, 7, 5);
		testSolver.getTheGameBoard().setACell(5, 7, 6);
		testSolver.getTheGameBoard().setACell(2, 8, 8);
		testSolver.getTheGameBoard().setACell(5, 8, 2);
		testSolver.getTheGameBoard().setACell(6, 8, 9);
		testSolver.getTheGameBoard().setACell(8, 8, 6);
		testSolver.getTheGameBoard().setACell(1, 9, 7);
		testSolver.getTheGameBoard().setACell(4, 9, 4);
	}

}
