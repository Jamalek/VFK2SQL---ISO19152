package output.other;

import java.util.ArrayList;

public class HP implements Comparable<HP> {
	public final long id;
	public final long par_id;
	public final char direction;
	public ArrayList<SOBR> sobrList = new ArrayList<SOBR>();
	
	public HP(long id, long par_id, String direction) {
		this.id = id;
		this.par_id = par_id;
		this.direction = direction.charAt(0);
	}

	@Override
	public int compareTo(HP p) {
		long comp = id - p.id;
		if (comp < 0) {
			return -1;
		} else if(comp > 0) {
			return 1;
		} else return 0;
	}

	public boolean isBefore(HP hp) {
		SOBR this_sobr1 = sobrList.get(0);
		SOBR hp_sobr1 = hp.sobrList.get(0);
		SOBR this_sobr2 = sobrList.get(sobrList.size()-1);
		SOBR hp_sobr2 = hp.sobrList.get(hp.sobrList.size()-1);
		if (direction == '+' && hp.direction == '+') {
			if (this_sobr1 == hp_sobr2) return true;
			else return false;
		} else if (direction == '+' && hp.direction == '-') {
			if (this_sobr1 == hp_sobr1) return true;
			else return false;
		} else if (direction == '-' && hp.direction == '+') {
			if (this_sobr2 == hp_sobr2) return true;
			else return false;
		} else { //direction == '-' && hp.direction == '-'
			if (this_sobr2 == hp_sobr1) return true;
			else return false;
		}
	}

	public boolean isAfter(HP hp) {
		SOBR this_sobr1 = sobrList.get(0);
		SOBR hp_sobr1 = hp.sobrList.get(0);
		SOBR this_sobr2 = sobrList.get(sobrList.size()-1);
		SOBR hp_sobr2 = hp.sobrList.get(hp.sobrList.size()-1);
		if (direction == '+' && hp.direction == '+') {
			if (this_sobr2 == hp_sobr1) return true;
			else return false;
		} else if (direction == '+' && hp.direction == '-') {
			if (this_sobr2 == hp_sobr2) return true;
			else return false;
		} else if (direction == '-' && hp.direction == '+') {
			if (this_sobr1 == hp_sobr1) return true;
			else return false;
		} else { //direction == '-' && hp.direction == '-'
			if (this_sobr1 == hp_sobr2) return true;
			else return false;
		}
	}
}
