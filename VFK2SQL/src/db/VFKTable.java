package db;

import java.util.ArrayList;

import output.Console;

public class VFKTable {
	public final String name;
	public final String[] head;
	private final ArrayList<String[]> rows;
	
	public VFKTable(String name, String[] head) {
		this.name = name;
		this.head = head;
		this.rows = new ArrayList<String[]>();
	}
	
	public void addRow(String[] tableRow) {
//		if (tableRow.length != head.length) {
//			Console.printLn("Nesouhlasi delka radku s delkou hlavicky v tabulce "+name+" "+tableRow.length+" "+head.length);
//		} else rows.add(tableRow);
		rows.add(tableRow);
	}
	
	public String[] getRow(int i) {
		return rows.get(i);
	}
	
	public ArrayList<String[]> getRows() {
		return rows;
	}
}
