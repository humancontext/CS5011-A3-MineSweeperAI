import java.util.ArrayList;
import java.util.Random;

/**
 * The agent class implemented RGS and SPS.
 * @author 170024030
 */
public class Agent_RGS_SPS {
	protected int rgsCount = 0;
	protected int spsCount = 0;
	protected char[][] coveredMap;
	protected int[][] answerMap;
	protected int nettleToMark = 0;
	protected int maxX;
	protected int maxY;
	public ArrayList<int[]> unknown = new ArrayList<int[]>();
	final char SIGN_UNKNOWN = '?';
	final char SIGN_NETTLE = '*';
	final char SIGN_MARK = 'N';
	public boolean isSafe = true;
	
	//the frontiers of uncovered cells
	public ArrayList<int[]> frontKnown = new ArrayList<int[]>();
	//the frontiers of covered cells
	public ArrayList<int[]> frontUnknown = new ArrayList<int[]>();
	
	/**
	 * constructor
	 * @param map
	 */
	public Agent_RGS_SPS(int[][] map) {
		this.answerMap = map;
		maxX = map.length;
		maxY = map[0].length;
		coveredMap = new char[maxX][maxY];
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				coveredMap[i][j] = SIGN_UNKNOWN;
				if (map[i][j] == -1) {
					nettleToMark++;
				}
				unknown.add(new int[]{i, j});
			}
		}
	}
	
	
	/**
	 * probe the adjacent cells of 0
	 * @param x
	 * @param y
	 */
	public void discoverZeroNeighbors(int x, int y) {
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < maxX && j >= 0 && j < maxY && coveredMap[i][j] == SIGN_UNKNOWN) {
					probe(i, j);
				}
			}
		}
	}
	
	/**
	 * probe a cell at (x,y)
	 * @param x
	 * @param y
	 */
	public void probe(int x, int y) {
		if (answerMap[x][y] == -1) {
			isSafe = false;
			coveredMap[x][y] = SIGN_NETTLE;
		}
		else {
			coveredMap[x][y] =  (char) ('0' + answerMap[x][y]);
		}
		for (int i = 0; i < unknown.size(); i++) {
			if (unknown.get(i)[0] == x && unknown.get(i)[1] == y) {
				unknown.remove(i);
				break;
			}
		}
		if (answerMap[x][y] == 0) {
			discoverZeroNeighbors(x, y);
		}
	}	
	
	/**
	 * mark a cell at (x,y)
	 * @param x
	 * @param y
	 */
	public void mark(int x, int y) {
		coveredMap[x][y] = SIGN_MARK;
		nettleToMark--;
		for (int i = 0; i < unknown.size(); i++) {
			if (unknown.get(i)[0] == x && unknown.get(i)[1] == y) {
				unknown.remove(i);
				break;
			}
		}
	}
	
	
	/**
	 * show the current state
	 */
	public void showMap() {
//		System.out.print("  ");
//		for (int i = 0; i < maxY; i++) {
//			System.out.print(i);
//			System.out.print(' ');
//		}
		System.out.println();
		for (int i = 0; i < maxX; i++) {
//			System.out.print(i + " ");
			for (int j = 0; j < maxY; j++) {
				System.out.print(coveredMap[i][j]);
				System.out.print(' ');
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * make a random guess for all covered cells
	 */
	public void rgs() {
		ArrayList<int[]> front = this.unknown;
		Random rand = new Random();
		int index = rand.nextInt(front.size());
		int loc[] = front.get(index);
		probe(loc[0], loc[1]);
		rgsCount++;
		System.out.println("RGS: probe[" +loc[0] + "," + loc[1] + "]");
		showMap();
	}
	
	/**
	 * single point strategy is implemented
	 * @return
	 */
	public boolean sps() {
		updateFrontUnknown();
		boolean successful = false;
		for (int i = 0; i < frontUnknown.size(); i++) {
			int x = frontUnknown.get(i)[0];
			int y = frontUnknown.get(i)[1];
			ArrayList<int[]> knownNeighbors = findAdjacentSafe(frontUnknown.get(i));
			for (int[] j: knownNeighbors) {
				//all clear neighbours
				if (answerMap[j[0]][j[1]] == findAdjacentMark(j).size()) {
					probe(x, y);
					successful = true;
					System.out.println("SPS: probe[" + x + "," + y + "]");
					spsCount++;
					showMap();
					i--;
					updateFrontUnknown();
					break;
				}
				else {
					//all marked neighbours
					if (answerMap[j[0]][j[1]] == findAdjacentRisk(j).size()) {
						mark(x, y);
						successful = true;
						System.out.println("SPS: probe[" + x + "," + y + "]");
						spsCount++;
						showMap();
						i--;
						updateFrontUnknown();
						break;
					}
				}
			}
		}
		return successful;
	}
	
	/**
	 * get a array list containing locations of adjacent safe cells 
	 * @param pair
	 * @return
	 */
	protected ArrayList<int[]> findAdjacentSafe(int[] pair) {
		int x = pair[0];
		int y = pair[1];
		ArrayList<int[]> neighbors  = new ArrayList<int[]>();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < maxX && j >= 0 && j < maxY && coveredMap[i][j] != SIGN_UNKNOWN && coveredMap[i][j] != SIGN_MARK) {
					neighbors.add(new int[]{i, j});
				}
			}
		}
		return neighbors;
	}
	
	/**
	 * get a array list containing locations of adjacent marked cells 
	 * @param pair
	 * @return
	 */
	protected ArrayList<int[]> findAdjacentMark(int[] pair) {
		int x = pair[0];
		int y = pair[1];
		ArrayList<int[]> neighbors  = new ArrayList<int[]>();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < maxX && j >= 0 && j < maxY && coveredMap[i][j] == SIGN_MARK) {
					neighbors.add(new int[]{i, j});
				}
			}
		}
		return neighbors;
	}
	
	/**
	 * get a array list containing locations of adjacent covered cells 
	 * @param pair
	 * @return
	 */
	protected ArrayList<int[]> findAdjacentUnknown(int[] pair) {
		int x = pair[0];
		int y = pair[1];
		ArrayList<int[]> neighbors  = new ArrayList<int[]>();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < maxX && j >= 0 && j < maxY && coveredMap[i][j] == SIGN_UNKNOWN) {
					neighbors.add(new int[]{i, j});
				}
			}
		}
		return neighbors;
	}
	
	/**
	 * get a array list containing locations of adjacent covered cells and marked cells
	 * @param pair
	 * @return
	 */
	protected ArrayList<int[]> findAdjacentRisk(int[] pair) {
		int x = pair[0];
		int y = pair[1];
		ArrayList<int[]> neighbors  = new ArrayList<int[]>();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < maxX && j >= 0 && j < maxY ) {
					if (coveredMap[i][j] == SIGN_UNKNOWN || coveredMap[i][j] == SIGN_MARK) {
						neighbors.add(new int[]{i, j});
					}
				}
			}
		}
		return neighbors;
	}
	
	/**
	 * update the covered front array list.
	 */
	public void updateFrontUnknown() {
		frontUnknown = new ArrayList<int[]>();
		for (int i = 0; i < unknown.size(); i++) {
			int[] pair = unknown.get(i).clone();
			if (findAdjacentSafe(pair).size() != 0) {
				frontUnknown.add(pair);
			}
		}
	}
	
	/**
	 * update the safe front array list.
	 */
	public void updateFrontKnown() {
		frontKnown = new ArrayList<int[]>();
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				if (coveredMap[i][j] != SIGN_UNKNOWN && coveredMap[i][j] != SIGN_MARK && findAdjacentUnknown(new int[]{i, j}).size() > 0) {
					frontKnown.add(new int[]{i,j});
				}
			}
		}
	}
}
