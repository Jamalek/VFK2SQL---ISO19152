package api;

import output.Help;

public class Api {
	public static void input(String[] args) {
		for (String string : args) {
			if (string.equalsIgnoreCase("-help")) Help.sendHelp();
		}
		if (args.length == 2) {
			String inputPath = args[0];
			String outputPath = args[1];
			new input.InputProcessor(inputPath, outputPath);
		} else output.Console.printLn("Spatny format vstupu");
	}
}
