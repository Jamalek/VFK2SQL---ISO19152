package output;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import db.LADMTable;
import db.LADMTableOperator;
import db.VFKTable;
import db.VFKTableOperator;
import exceptions.ColumnNotFoundException;
import exceptions.TableNotFoundException;
import output.other.*;

public class OutputCreator {
	private final VFKTableOperator vto;
	private final LADMTableOperator lto;
	
	public OutputCreator(String outputPath, VFKTableOperator vfkTableOperator) {
		this.vto = vfkTableOperator;
		this.lto = new LADMTableOperator();
		createOutput();
		Printer.print(new File(outputPath), lto);
	}

	private void createOutput() {
		try {
			la_point();
			corner();
			la_BoundaryFaceString();
			boundary();
			la_SpatialUnit();
		} catch (TableNotFoundException e) {
			e.printStackTrace();
		} catch (ColumnNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private SOBR[] sobr;
	private SBP[] sbp;
	private HP[] hp;
	private PAR[] par;

	private void la_point() throws ColumnNotFoundException, TableNotFoundException {
		LADMTable ladmTable = lto.createTable("LA_Point", new String[] {
				"pID", "NUMBER NOT NULL",
				"point", "SDO_GEOMETRY NOT NULL",
				"PRIMARY KEY", "(pID)"
				});
		VFKTable vfkTable = vto.getTable("SOBR");
		int pID = vto.getColumnByName("ID N30", vfkTable);
		int x = vto.getColumnByName("SOURADNICE_X N10.2", vfkTable);
		int y = vto.getColumnByName("SOURADNICE_Y N10.2", vfkTable);
		for (String[] row : vfkTable.getRows()) {
			ladmTable.addRow(new String[] {
					row[pID],
					"SDO_GEOMETRY("
					+ "2001,"
					+ "4156,"
					+ "SDO_POINT_TYPE(-"+row[y]+", -"+row[x]+", NULL),"
					+ "NULL,"
					+ "NULL)"
			});
		}
	}

	private void corner() throws ColumnNotFoundException, TableNotFoundException {
		LADMTable ladmTable = lto.createTable("Corner", new String[] {
				"boundaryID", "NUMBER NOT NULL",
				"sequence", "NUMBER",
				"pID", "NUMBER NOT NULL"
				});
		VFKTable vfkTable = vto.getTable("SBP");
		int boundaryID = vto.getColumnByName("HP_ID N30", vfkTable);
		int sequence = vto.getColumnByName("PORADOVE_CISLO_BODU N38", vfkTable);
		int pID = vto.getColumnByName("BP_ID N30", vfkTable);
		for (String[] row : vfkTable.getRows()) {
			if (!row[boundaryID].equalsIgnoreCase("")) {
				ladmTable.addRow(new String[] {
						row[boundaryID],
						row[sequence],
						row[pID]
				});
			}
		}
	}

	private void la_BoundaryFaceString() throws TableNotFoundException, ColumnNotFoundException {
		LADMTable ladmTable = lto.createTable("LA_BoundaryFaceString", new String[] {
				"bfsID", "NUMBER NOT NULL"
				});
		VFKTable vfkTable = vto.getTable("HP");
		int bfsID = vto.getColumnByName("ID N30", vfkTable);
		for (String[] row : vfkTable.getRows()) {
			ladmTable.addRow(new String[] {
					row[bfsID]
			});
		}
	}

	private void boundary() throws ColumnNotFoundException, TableNotFoundException {
		VFKTable t_hp = vto.getTable("HP");
		VFKTable t_sbp = vto.getTable("SBP");
		VFKTable t_sobr = vto.getTable("SOBR");
		VFKTable t_par = vto.getTable("PAR");
		long[] par_id = vto.getColumnAsLongField("ID N30", t_par);
		long[] hp_par_id1 = vto.getColumnAsLongField("PAR_ID_1 N30", t_hp);
		long[] hp_par_id2 = vto.getColumnAsLongField("PAR_ID_2 N30", t_hp);
		long[] hp_id = vto.getColumnAsLongField("ID N30", t_hp);
		long[] sbp_hp_id = vto.getColumnAsLongField("HP_ID N30", t_sbp);
		long[] sbp_sobr_id = vto.getColumnAsLongField("BP_ID N30", t_sbp);
		int[] sbp_porad = vto.getColumnAsIntegerField("PORADOVE_CISLO_BODU N38", t_sbp);
		long[] sobr_id = vto.getColumnAsLongField("ID N30", t_sobr);
		String[] sobr_x = vto.getColumnAsStringField("SOURADNICE_Y N10.2", t_sobr);
		String[] sobr_y = vto.getColumnAsStringField("SOURADNICE_X N10.2", t_sobr);
		sobr = new SOBR[sobr_id.length];
		sbp = new SBP[sbp_porad.length];
		hp = new HP[hp_id.length*2];
		par = new PAR[par_id.length];
		for (int i = 0; i < sobr_id.length; i++) {
			sobr[i] = new SOBR(sobr_id[i], sobr_x[i], sobr_y[i]);
		}
		for (int i = 0; i < sbp_porad.length; i++) {
			sbp[i] = new SBP(sbp_sobr_id[i], sbp_hp_id[i], sbp_porad[i]);
		}
		for (int i = 0; i < hp_id.length; i++) {
			hp[i] = new HP(hp_id[i], hp_par_id1[i], "+");
		}
		for (int i = 0; i < hp_id.length; i++) {
			hp[i+hp_id.length] = new HP(hp_id[i], hp_par_id2[i], "-");
		}
		for (int i = 0; i < par.length; i++) {
			par[i] = new PAR(par_id[i]);
		}
		Arrays.sort(sobr);
		Arrays.sort(sbp);
		Arrays.sort(hp);
		Arrays.sort(par);
		for (int i = 0, j = 0; i < sbp.length;) {
			if (sbp[i].sobr_id == sobr[j].id) {
				sbp[i].sobr = sobr[j];
				i++;
			} else if (sbp[i].sobr_id > sobr[j].id) {
				j++;
			} else {
				Console.printLn("Nebyl nalezen vrchol parcely");
				System.exit(0);
			}
		}
		Arrays.sort(sbp, new Comparator<SBP>() {
			@Override
			public int compare(SBP o1, SBP o2) {
				long comp = o1.hp_id - o2.hp_id;
				int comp2 = o1.porad - o2.porad;
				if (comp < 0) return -1;
				else if (comp > 0) return 1;
				else if (comp2 < 0) return -1;
				else if (comp2 > 0) return 1;
				else return 0;
			}
		});
		for (int i = 0, j = 0; i < hp.length && j < sbp.length;) {
			if (hp[i].id == sbp[j].hp_id) {
				hp[i].sobrList.add(sbp[j].sobr);
				hp[i+1].sobrList.add(sbp[j].sobr);
				j++;
			} else if (hp[i].id > sbp[j].hp_id) {
				j++;
			} else { //hp[i].id < sbp[j].hp_id
				i+=2;
			}
		}
		Arrays.sort(hp, new Comparator<HP>() {
			@Override
			public int compare(HP o1, HP o2) {
				long comp = o1.par_id - o2.par_id;
				if (comp < 0) {
					return -1;
				} else if(comp > 0) {
					return 1;
				} else return 0;
			}
		});
		for (int i = 0, j = 0; i < par.length && j < hp.length;) {
			if (par[i].id == hp[j].par_id) {
				par[i].boundaryParts.add(hp[j]);
				j++;
			} else if (par[i].id > hp[j].par_id) {
				j++;
			} else {
				i++;
			}
		}
		LADMTable ladmTable = lto.createTable("Boundary", new String[] {
				"bfsID", "NUMBER NOT NULL",
				"suID", "NUMBER NOT NULL",
				"sequenceNumber", "NUMBER NOT NULL",
				"direction", "CHAR NOT NULL"
				});
		for (PAR parcela : par) {
			parcela.sortBoundaryParts();
			int i = 1;
			for (ArrayList<HP> hpList : parcela.boundaryPartsSorted) {
				for (HP hp2 : hpList) {
					ladmTable.addRow(new String[] {
							String.valueOf(hp2.id),
							String.valueOf(parcela.id),
							String.valueOf(i),
							"'"+String.valueOf(hp2.direction)+"'"
					});
					i++;
				}
			}
		}
	}

	private void la_SpatialUnit() throws ColumnNotFoundException, TableNotFoundException {
		LADMTable ladmTable = lto.createTable("LA_SpatialUnit", new String[] {
				"suID", "NUMBER NOT NULL",
				"bpsi", "VARCHAR2(100) NOT NULL",
				});
		for (PAR par2 : par) {
			String bpbiString = par2.boundaryPartsBeginIndex.toString();
			ladmTable.addRow(new String[] {
					String.valueOf(par2.id),
					"'"+bpbiString.substring(1, bpbiString.length()-1)+"'"
			});
		}
	}
}
