import java.util.ArrayList;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * The agent class implemented RGS, SPS and DLS.
 * @author 170024030
 *
 */
public class Agent_RGS_SPS_DLS extends Agent_RGS_SPS{
	protected int dlsCount = 0;
	private static DPLLSatisfiable dpll = new DPLLSatisfiable();
	
	/**
	 * constructor of agent.
	 * @param map
	 */
	public Agent_RGS_SPS_DLS(int[][] map) {
		super(map);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * The Davis-Putnam-Logemann-Loveland algorithm implemented
	 * @return true if one or more cells are successfully probed or marked
	 */
	public boolean dls() {
		updateFrontKnown();
		updateFrontUnknown();
		boolean successful = false;
		//generate the KBU with all known frontiers
		String KBU = getKBU();
		//for each unknown frontier, run the positive DPLLSat and negative DPLLSat
		for (int i = 0; i < frontUnknown.size(); i++) {
			int x = frontUnknown.get(i)[0];
			int y = frontUnknown.get(i)[1];
			//generate the logic sentence for this unknown frontier
			String p = "N_" + x + "_" + + y;
			//negative DPLLSat
			String prove = KBU + " & ~" + p;
			boolean ans = displayDPLLSatisfiableStatus(prove);
			if (!ans) {
				//if the answer is false, mark this cell.
				System.out.println("DLS: mark[" + x + "," + y + "]");
				dlsCount++;
				mark(x, y);
				successful = true;
				showMap();
			}
			else {
				//positive DPLLSat
				prove = KBU + " & " + p;
				ans = displayDPLLSatisfiableStatus(prove);
				if (!ans) {
					//if the answer is false, probe the cell
					System.out.println("DLS: probe[" + x + "," + y + "]");
					dlsCount++;
					probe(x, y);
					successful = true;
					showMap();
				}
			}
		}
		return successful;
	}
	
	/**
	 * create the KBU.
	 * @return
	 */
	private String getKBU() {
		String KBU = "";
		//generate logic proposition for single known frontier and link them with &
		for (int i = 0; i < frontKnown.size(); i++) {
			int[] cell = frontKnown.get(i);
			String sentence = getSingleKBU(cell);
			KBU += sentence;
			if (i < frontKnown.size() - 1) {
				KBU += " & ";
			}
		}
		
		return KBU;
	}
	
	/**
	 * create the logic proposition for a single known cell
	 * @param cell
	 * @return
	 */
	private String getSingleKBU(int[] cell) {
		String single = "(";
		int x = cell[0];
		int y = cell[1];
		//count the unmarked nettles around this cell.
		int count = answerMap[x][y] - findAdjacentMark(cell).size();
		ArrayList<int[]> unknownNeighbors = findAdjacentUnknown(cell);
		//get a collection of list containing all possible combination of 0 and 1.
		//1 is nettle-positive
		//0 is nettle-negative
		//the size of this combination is the number of covered cells around it.
		int[] set = new int[unknownNeighbors.size()];
		for (int i = 0; i < set.length; i++) {
			set[i] = 0;
		}
		ArrayList<int[]> possibleSet = new ArrayList<int[]>();
		possibleSet.add(set.clone());
		for (int i = 0; i < Math.pow(2, set.length) - 1; i++) {
			int index = 0;
			set[index]++;
			while(set[index] == 2) {
				set[index] = 0;
				index++;
				set[index]++;
			}
			possibleSet.add(set.clone());
		}
		//leave combinations with correct number of nettle only
		for (int i = 0; i < possibleSet.size(); i++) {
			int sum = 0;
			for (int j: possibleSet.get(i)) {
				if (j == 1) sum++;
			}
			if (sum != count) {
				possibleSet.remove(i);
				i--;
			}
		}
		//generate a proposition for this cell from the binary list
		for (int j = 0; j < possibleSet.size(); j++) {
			int[] array = possibleSet.get(j);
			String s = "(";
			for(int i = 0; i < array.length; i++) {
				if (array[i] == 0) {
					s += "~";
				}
				s += "N_";
				s += unknownNeighbors.get(i)[0];
				s += "_";
				s += unknownNeighbors.get(i)[1];
				if (i != array.length - 1) {
					s += " & ";
				}
			}
			s += ")";
			if (j != possibleSet.size() - 1) {
				s += " | ";
			}
			single += s;
		}
		single += ")";
		return single;
	}
	
	/**
	 * this method is given in the lecture slide, invoking the DPLLSat judgement method from the library.
	 * @param query
	 * @return
	 */
	public static boolean displayDPLLSatisfiableStatus(String query) {
		PLParser parser = new PLParser();
		Sentence sent = (Sentence) parser.parse(query);
		if (dpll.dpllSatisfiable(sent)) {
			System.out.println(query + " is satisfiable");
			return true;
		} 
		else {
			System.out.println(query + " is NOT satisfiable");
			return false;
		}
	}
}
