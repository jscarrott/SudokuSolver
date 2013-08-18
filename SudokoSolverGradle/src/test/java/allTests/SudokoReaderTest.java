package allTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import sudoko.SudokoReader;

public class SudokoReaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		SudokoReader reader = new SudokoReader();
		try {
			for(Integer integer : reader.readInSudoko()){
				System.out.println(integer);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
