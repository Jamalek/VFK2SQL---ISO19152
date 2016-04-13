package output.other;

public class SBP implements Comparable<SBP> {
	public final long sobr_id;
	public final long hp_id;
	public final int porad;
	public SOBR sobr;
	
	public SBP(long id_sobr, long id_hp, int porad) {
		this.sobr_id = id_sobr;
		this.hp_id = id_hp;
		this.porad = porad;
	}

	@Override
	public int compareTo(SBP p) {
		long comp = sobr_id - p.sobr_id;
		if (comp < 0) {
			return -1;
		} else if(comp > 0) {
			return 1;
		} else return 0;
	}
}