package input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

import db.VFKTableOperator;
import output.Console;
import output.OutputCreator;

public class InputProcessor {
	public InputProcessor(String inputPath, String outputPath) {
		this.inputPath = inputPath;
		fileInput = new File(inputPath);
		vfkTableOperator = new VFKTableOperator();
		processInput();
		new OutputCreator(outputPath, vfkTableOperator);
	}

	private final String inputPath;
	private final File fileInput;
	private final VFKTableOperator vfkTableOperator;
	
	private void processInput() {
		Scanner sc = null;
		try {
			sc = new Scanner(new FileReader(fileInput));
		} catch (FileNotFoundException e) {
			Console.printLn("Problém se čtením vstupního souboru: "+inputPath);
			System.exit(0);
		}
		while (sc.hasNext()) {
			processLine(sc.nextLine());
		}
	}

	private void processLine(String line) {
		switch (line.charAt(1)) {
		case 'H':
			processH(line.substring(2));
			break;
		case 'D':
			processD(line.substring(2));
			break;
		case 'B':
			processB(line.substring(2));
			break;
		default:
			break;
		}
	}
	
	private void processH(String substring) {
		//TODO processH
	}

	private void processB(String substring) {
		String[] cellsString = substring.split(";");
		String tableName = cellsString[0];
		String[] tableHead = Arrays.copyOfRange(cellsString, 1, cellsString.length);
		vfkTableOperator.createTable(tableName, tableHead);
	}

	private void processD(String substring) {
		try {
			String[] cellsString = substring.split(";");
			String[] tableRow = Arrays.copyOfRange(cellsString, 1, cellsString.length);
			vfkTableOperator.getCurrentTable().addRow(tableRow);
		} catch (Exception e) {
			// Jeste nejsme v tabulce
		}
	}
}
