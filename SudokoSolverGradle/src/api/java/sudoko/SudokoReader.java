package sudoko;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

public class SudokoReader {
	
	
	private ArrayList<String> numbers;
	
	
	public ArrayList<Integer> readInSudoko() throws FileNotFoundException{
		numbers = new ArrayList<>();
		File sudokoFile  = new File("sudoko.txt");//use correct directory
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(sudokoFile));
		Scanner inputNumbers = new Scanner(is);
		inputNumbers.useDelimiter("\n");
		while(inputNumbers.hasNextLine()){
			String line = inputNumbers.next();
			String[] eachNumberArray = StringUtils.split(line);
			for(int counter = 0; counter <= 8; counter++){
				numbers.add(eachNumberArray[counter]);
			}
			
		}
		inputNumbers.close();
		ArrayList<Integer> integerNumbers = new ArrayList<>();
		for(String number : numbers){
			integerNumbers.add(Integer.parseInt(number));
		}
		return integerNumbers;
	}

}
