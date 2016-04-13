package output.other;

import java.util.ArrayList;

import input.InputProcessor;
import output.Console;

public class PAR implements Comparable<PAR> {
	public static void main(String[] args) {
		new InputProcessor("VstupTest.vfk", "VystupTest.txt");
	}

	public final ArrayList<HP> boundaryParts = new ArrayList<HP>();
	public ArrayList<ArrayList<HP>> boundaryPartsSorted = new ArrayList<ArrayList<HP>>();
	public final long id;
	
	public PAR(long id) {
		this.id = id;
	}
	
	public void putBorderlineFirst() {
		for (int i = 0; i < boundaryPartsSorted.size(); i++) {
			ArrayList<Double> x = new ArrayList<Double>();
			ArrayList<Double> y = new ArrayList<Double>();
			ArrayList<HP> bp = boundaryPartsSorted.get(i);
			for (int j = 0; j < bp.size(); j++) {
				HP hp = bp.get(j);
				switch (getDirection(i, j)) {
				case '-':
					for (int k = 1; k < hp.sobrList.size(); k++) {
						x.add(getX(i, j, k));
						y.add(getY(i, j, k));
					}
					break;
				case '+':
					for (int k = hp.sobrList.size()-2; k >= 0; k--) {
						x.add(getX(i, j, k));
						y.add(getY(i, j, k));
					}
					break;
				default:
					break;
				}
			}
			System.out.println(x);
			System.out.println(y);
			double uhel = 0;
			uhel += uhel(x.get(x.size()-1), y.get(y.size()-1), x.get(0), y.get(0), x.get(1), y.get(1));
			for (int j = 1; j < x.size()-1; j++) {
				uhel += uhel(x.get(j-1), y.get(j-1), x.get(j), y.get(j), x.get(j+1), y.get(j+1));
			}
			uhel += uhel(x.get(x.size()-2), y.get(y.size()-2), x.get(x.size()-1), y.get(y.size()-1), x.get(0), y.get(0));
			System.out.println(uhel);
		}
		System.out.println("------");
	}
	
	private double uhel(Double x1, Double y1, Double x2, Double y2, Double x3, Double y3) {
		double uhel = smernik(x2,y2,x1,y1)-smernik(x2,y2,x3,y3);
		return (uhel >= 0) ? uhel : uhel + 2;
	}

	private double smernik(Double x1, Double y1, Double x2, Double y2) {
		double param = (x2-x1)/(y2-y1);
		if (x2 >= x1 && y2 >= y1) {
			return Math.atan(param)/Math.PI;
		} else if (x2 >= x1 && y2 < y1) {
			return 1 - Math.atan(-param)/Math.PI;
		} else if (x2 < x1 && y2 < y1) {
			return 1 + Math.atan(param)/Math.PI;
		} else {//(x2 < x1 && y2 >= y1)
			return 2 - Math.atan(-param)/Math.PI;
		}
			
	}

	private double getX(int border, int borderPart, int linePart) {
		return Double.parseDouble(boundaryPartsSorted.get(border).get(borderPart).sobrList.get(linePart).x);
	}
	
	private double getY(int border, int borderPart, int linePart) {
		return Double.parseDouble(boundaryPartsSorted.get(border).get(borderPart).sobrList.get(linePart).y);
	}
	
	private char getDirection(int border, int borderPart) {
		return boundaryPartsSorted.get(border).get(borderPart).direction;
	}
	
	public void sortBoundaryParts() {
		ArrayList<HP> bpu = new ArrayList<HP>();
		ArrayList<ArrayList<HP>> bps = boundaryPartsSorted;
		bpu.addAll(boundaryParts);
		bps.add(new ArrayList<HP>());
		bps.get(0).add(bpu.get(0));
		bpu.remove(0);
		int n = 0;
		while (bpu.size() > 0) {
			if (n++ > 10000) {
				Console.printLn("Chyba pri skladani parcel");
				System.exit(0);
			}
			boolean found = false;
			for (int i = 0; i < bpu.size(); i++) {
				for (ArrayList<HP> bp : bps) {
					if (bpu.get(i).isBefore(bp.get(0))) {
						bp.add(0, bpu.get(i));
						bpu.remove(bpu.get(i));
						found = true;
						break;
					} else if (bpu.get(i).isAfter(bp.get(bp.size()-1))) {
						bp.add(bpu.get(i));
						bpu.remove(bpu.get(i));
						found = true;
						break;
					} 
				}
			}
			if (!found) {
				bps.add(new ArrayList<HP>());
				bps.get(bps.size()-1).add(bpu.get(0));
				bpu.remove(0);
			}
		}
	}

	@Override
	public int compareTo(PAR p) {
		long comp = id - p.id;
		if (comp < 0) {
			return -1;
		} else if(comp > 0) {
			return 1;
		} else return 0;
	}
}
