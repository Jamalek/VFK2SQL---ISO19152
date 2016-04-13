package db;

import java.util.ArrayList;

import exceptions.TableNotFoundException;

public class LADMTableOperator {
	private ArrayList<LADMTable> tables = new ArrayList<LADMTable>();

	public LADMTable createTable(String tableName, String[] tableHead) {
		LADMTable ladmTable = new LADMTable(tableName, tableHead);
		tables.add(ladmTable);
		return ladmTable;
	}

	public LADMTable getCurrentTable() {
		return tables.get(tables.size()-1);
	}
	
	public LADMTable getTable(String tableName) throws Exception {
		for (LADMTable vfkTable : tables) {
			if (vfkTable.name.equalsIgnoreCase(tableName)) {
				return vfkTable;
			}
		} 
		throw new TableNotFoundException();
	}

	public ArrayList<LADMTable> getTables() {
		return tables;
	}
}
