import java.util.ArrayList;

/**
 * The agent class implemented RGS,SPS and EES.
 * @author 170024030
 *
 */
public class Agent_RGS_SPS_EES extends Agent_RGS_SPS{
	protected int eesCount = 0;
	/**
	 * constructor.
	 * @param map
	 */
	public Agent_RGS_SPS_EES(int[][] map) {
		super(map);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Implementation of the Easy Equation Strategy
	 * @return true if one or more cells are successfully probed or marked
	 */
	public boolean ees() {
		updateFrontKnown();
		boolean successful = false;
		for (int i = 0; i < frontKnown.size() - 1; i++) {
			for (int j = i + 1; j < frontKnown.size(); j++) {
				//for each pairs of uncovered cells
				//if one's covered neighbours are contained by the other's
				//carry out the easy equation analysis
				int[] front1 = frontKnown.get(i);
				int[] front2 = frontKnown.get(j);
				ArrayList<int[]> n1 = findAdjacentUnknown(front1);
				ArrayList<int[]> n2 = findAdjacentUnknown(front2);
				int ic = isContained(n1, n2);
				if (ic != 0) {
					int diff = Math.abs((answerMap[front1[0]][front1[1]] - findAdjacentMark(front1).size()) - (answerMap[front2[0]][front2[1]] - findAdjacentMark(front2).size()));
					if (diff == 0) {
						if (ic == 1) probeEes(n2, n1);
						else probeEes(n1, n2);
						successful = true;
						showMap();
					}
					if (diff == Math.abs(n1.size() - n2.size())) {
						if (ic == 1) markEes(n2, n1);
						else markEes(n1, n2);
						successful = true;
						showMap();
					}
				}
			}
		}
		return successful;
	}
	
	/**
	 * judge if the one of the array contains the other
	 * @param n1
	 * @param n2
	 * @return 0: not contained; 1:n2 contains n1; 2 n1 contains n2.
	 */
	public int isContained(ArrayList<int[]> n1, ArrayList<int[]> n2) {
		int count = 0;
		if (n1.size() == n2.size()) return 0;
		for (int[] i : n1) {
			for (int[] j : n2) {
				if(i[0] == j[0] && i[1] == j[1]) {
					count++;
				}
			}
		}
		if (count == n1.size()) return 1;
		else {
			if (count == n2.size()) return 2;
			else return 0;
		}
	}
	
	/**
	 * probe all cells that only belong to the bigger collection.
	 * @param n1	bigger
	 * @param n2	smaller
	 */
	public void probeEes(ArrayList<int[]> n1, ArrayList<int[]> n2) {
		for (int[] i : n1) {
			boolean contained = false;
			for (int[] j : n2) {
				if(i[0] == j[0] && i[1] == j[1]) {
					contained = true;
				}
			}
			if (!contained) {
				System.out.println("EES: probe[" + i[0] + "," + i[1] + "]");
				eesCount++;
				probe(i[0], i[1]);
			}
		}
	}
	
	/**
	 * probe all cells that only belong to the bigger collection.
	 * @param n1	bigger
	 * @param n2	smaller
	 */
	public void markEes(ArrayList<int[]> n1, ArrayList<int[]> n2) {
		for (int[] i : n1) {
			boolean contained = false;
			for (int[] j : n2) {
				if(i[0] == j[0] && i[1] == j[1]) {
					contained = true;
				}
			}
			if (!contained) {
				System.out.println("EES: mark[" + i[0] + "," + i[1] + "]");
				eesCount++;
				mark(i[0], i[1]);
			}
		}
	}
}
