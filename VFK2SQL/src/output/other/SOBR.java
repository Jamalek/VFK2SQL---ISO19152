package output.other;

public class SOBR implements Comparable<SOBR> {
	public final long id;
	public final String x;
	public final String y;
	
	public SOBR(long sobr_id, String x, String y) {
		this.id = sobr_id;
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(SOBR p) {
		long comp = id - p.id;
		if (comp < 0) {
			return -1;
		} else if(comp > 0) {
			return 1;
		} else return 0;
	}
}
