package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import db.LADMTable;
import db.LADMTableOperator;
import db.VFKTableOperator;

public class Printer {
	private static PrintStream ps;
	

	public Printer(File fileOutput) {
		try {
			ps = new PrintStream(fileOutput);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void print(File fileOutput, LADMTableOperator lto) {
		try {
			ps = new PrintStream(fileOutput);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (LADMTable table : lto.getTables()) {
			String sql = "CREATE TABLE "+table.name+"(";
			String[] head = table.head;
			for (int i = 0; i < head.length; i+=2) {
				sql += table.head[i]+" "+table.head[i+1];
				if (i < head.length-2) sql += ", ";
			}
			sql += ");";
			ps.println(sql);
		}
		ps.println();
		for (LADMTable table : lto.getTables()) {
			for (String[] row : table.getRows()) {
				String sql = "INSERT INTO "+table.name+" VALUES(";
				for (int i = 0; i < row.length; i++) {
					sql += row[i];
					if (i < row.length-1) sql += ", ";
				}
				sql += ");";
				ps.println(sql);
			}
		}
		ps.close();
	}
}
