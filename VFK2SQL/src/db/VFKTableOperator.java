package db;

import java.util.ArrayList;

import exceptions.ColumnNotFoundException;
import exceptions.TableNotFoundException;

public class VFKTableOperator {
	private ArrayList<VFKTable> tables = new ArrayList<VFKTable>();

	public void createTable(String tableName, String[] tableHead) {
		tables.add(new VFKTable(tableName, tableHead));
	}

	public VFKTable getCurrentTable() {
		return tables.get(tables.size()-1);
	}
	
	public VFKTable getTable(String tableName) throws TableNotFoundException {
		for (VFKTable vfkTable : tables) {
			if (vfkTable.name.equalsIgnoreCase(tableName)) {
				return vfkTable;
			}
		} 
		throw new TableNotFoundException();
	}
	
	public int getColumnByName(String columnName, VFKTable vfkTable) throws ColumnNotFoundException, TableNotFoundException {
		for (int i = 0; i < vfkTable.head.length; i++) {
			if (vfkTable.head[i].equalsIgnoreCase(columnName)) return i;
		}
		throw new ColumnNotFoundException();
	}
	
	public int[] getColumnAsIntegerField(String columnName, VFKTable vfkTable) throws ColumnNotFoundException, TableNotFoundException {
		ArrayList<String[]> rows = vfkTable.getRows();
		int colID = getColumnByName(columnName, vfkTable);
		int[] col = new int[rows.size()];
		for (int i = 0; i < col.length; i++) {
			col[i] = Integer.parseInt(rows.get(i)[colID]);
		}
		return col;
	}

	public String[] getColumnAsStringField(String columnName, VFKTable vfkTable) throws ColumnNotFoundException, TableNotFoundException {
		ArrayList<String[]> rows = vfkTable.getRows();
		int colID = getColumnByName(columnName, vfkTable);
		String[] col = new String[rows.size()];
		for (int i = 0; i < col.length; i++) {
			col[i] = rows.get(i)[colID];
		}
		return col;
	}

	public long[] getColumnAsLongField(String columnName, VFKTable vfkTable) throws ColumnNotFoundException, TableNotFoundException {
		ArrayList<String[]> rows = vfkTable.getRows();
		int colID = getColumnByName(columnName, vfkTable);
		long[] col = new long[rows.size()];
		for (int i = 0; i < col.length; i++) {
			try {
				col[i] = Long.parseLong(rows.get(i)[colID]);
			} catch (ArrayIndexOutOfBoundsException e) {
				col[i] = -1; //asi prazdna bunka
			} catch (NumberFormatException e) {
				col[i] = -2; //asi chybi cislo
			}
		}
		return col;
	}
}
