/**
 * this class is used in SRGS method to rank the cells
 * @author xy31
 *
 */
public class PCell {
	public int[] location;
	public double pSafe;
	public PCell(int[] location, double pSafe) {
		this.location = location;
		this.pSafe = pSafe;
	}
}
