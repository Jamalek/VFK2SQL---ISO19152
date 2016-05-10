package output.other;

import java.util.ArrayList;

import input.InputProcessor;
import output.Console;

public class PAR implements Comparable<PAR> {
	public final ArrayList<HP> boundaryParts = new ArrayList<HP>();
	public ArrayList<ArrayList<HP>> boundaryPartsSorted = new ArrayList<ArrayList<HP>>();
	public final long id;
	public ArrayList<Integer> boundaryPartsBeginIndex = new ArrayList<Integer>();
	
	public PAR(long id) {
		this.id = id;
	}
	
	public boolean isOrientedClockWise(ArrayList<HP> bp) {
		int n = 0;
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		for (int j = 0; j < bp.size(); j++) {
			HP hp = bp.get(j);
			switch (hp.direction) {
			case '-':
				for (int k = 1; k < hp.sobrList.size(); k++) {
					x.add(getX(hp, k));
					y.add(getY(hp, k));
					n++;
				}
				break;
			case '+':
				for (int k = hp.sobrList.size()-2; k >= 0; k--) {
					x.add(getX(hp, k));
					y.add(getY(hp, k));
					n++;
				}
				break;
			default:
				break;
			}
		}
		double angle = 0;
		angle += angle(x.get(x.size()-1), y.get(y.size()-1), x.get(0), y.get(0), x.get(1), y.get(1));
		for (int j = 1; j < x.size()-1; j++) {
			angle += angle(x.get(j-1), y.get(j-1), x.get(j), y.get(j), x.get(j+1), y.get(j+1));
		}
		angle += angle(x.get(x.size()-2), y.get(y.size()-2), x.get(x.size()-1), y.get(y.size()-1), x.get(0), y.get(0));
		return (angle/n > 0) ? false : true;
	}
	
	private double angle(Double x1, Double y1, Double x2, Double y2, Double x3, Double y3) {
		double angle = smernik(x2,y2,x1,y1)-smernik(x2,y2,x3,y3);
		return (angle >= 0) ? angle : angle + 2;
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

	private double getX(HP hp, int linePart) {
		return Double.parseDouble(hp.sobrList.get(linePart).x);
	}
	
	private double getY(HP hp, int linePart) {
		return Double.parseDouble(hp.sobrList.get(linePart).y);
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
					if (bpu.get(i).isConnectedTo(bp.get(0))) {
						bp.add(0, bpu.get(i));
						bpu.remove(bpu.get(i));
						found = true;
						break;
					} else if (bpu.get(i).isConnectedTo(bp.get(bp.size()-1))) {
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
		for (ArrayList<HP> hpList : bps) {
			orientCounterClockWise(hpList);
		}
		outerBoundaryFirst();
		if (id == Long.parseLong("2851165306")) {
			for (ArrayList<HP> hpList : bps) {
				for (HP hp : hpList) {
					System.out.println(hp.sobrList.get(1).id);
				}
			}
		}
		for (int i = 1; i < bps.size(); i++) {
			ArrayList<HP> hpList = bps.get(i);
			switchOrientation(hpList);
		}
		addIndexArray();
	}
	
	private void orientCounterClockWise(ArrayList<HP> hpList) {
		for (int i = 0; i < hpList.size()-1; i++) {
			HP hp = hpList.get(i);
			HP hp1 = hpList.get(i+1);
			hp.direction = (hp.sobrList.get(0) == hp1.sobrList.get(0) || 
					hp.sobrList.get(0) == hp1.sobrList.get(hp1.sobrList.size()-1)) ?
							'-' : '+';
		}
		HP hp = hpList.get(hpList.size()-1);
		HP hp1 = hpList.get(0);
		hp.direction = (hp.sobrList.get(0) == hp1.sobrList.get(0) || 
				hp.sobrList.get(0) == hp1.sobrList.get(hp1.sobrList.size()-1)) ?
						'-' : '+';
		if (isOrientedClockWise(hpList)) {
			switchOrientation(hpList);
		}
	}

	private void switchOrientation(ArrayList<HP> hpList) {
		ArrayList<HP> hpListNew = new ArrayList<HP>();
		for (int i = hpList.size()-1; i >= 0; i--) {
			hpListNew.add(hpList.get(i));
		}
		for (HP hp : hpListNew) {
			hp.direction = (hp.direction == '+') ? '-' : '+';
		}
		for (int i = 0; i < hpListNew.size(); i++) {
			hpList.set(i, hpListNew.get(i));
		}
	}
	
	private void outerBoundaryFirst() {
		ArrayList<HP> b = getBoundaryWithLargestArea();
		ArrayList<ArrayList<HP>> boundaryPartsSortedOuter = new ArrayList<ArrayList<HP>>();
		boundaryPartsSortedOuter.add(b);
		for (int i = 0; i < boundaryPartsSorted.size(); i++) {
			if (boundaryPartsSorted.get(i) != b) boundaryPartsSortedOuter.add(boundaryPartsSorted.get(i));
		}
		boundaryPartsSorted = boundaryPartsSortedOuter;
	}

	private ArrayList<HP> getBoundaryWithLargestArea() {
		double maxArea = 0;
		ArrayList<HP> lab = null;
		for (ArrayList<HP> hpList : boundaryPartsSorted) {
			double a = getArea(hpList);
			if (a > maxArea) {
				maxArea = a;
				lab = hpList;
			}
		}
		return lab;
	}

	private double getArea(ArrayList<HP> hpList) {
		double a = 0;
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		for (HP hp : hpList) {
			if (hp.direction == '-') {
				for (int i = 0; i < hp.sobrList.size() -1; i++) {
					x.add(Double.parseDouble(hp.sobrList.get(i).x));
					y.add(Double.parseDouble(hp.sobrList.get(i).y));
				}
			} else {
				for (int i = hp.sobrList.size()-1; i > 0; i--) {
					x.add(Double.parseDouble(hp.sobrList.get(i).x));
					y.add(Double.parseDouble(hp.sobrList.get(i).y));
				}
			}
		}
		for (int i = 0,j; i < x.size(); i++) {
			j = (i + 1) % x.size();
			a += x.get(i)*y.get(j);
			a -= x.get(j)*y.get(i);
		}
		return Math.abs(a);
	}
	
	private void addIndexArray() {
		boundaryPartsBeginIndex.add(1);
		for (int i = 0; i < boundaryPartsSorted.size()-1; i++) {
			boundaryPartsBeginIndex.add(
					boundaryPartsBeginIndex.get(boundaryPartsBeginIndex.size()-1)+
					boundaryPartsSorted.get(i).size());
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
