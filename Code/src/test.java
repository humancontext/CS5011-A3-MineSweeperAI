import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;

public class test {
	private static DPLLSatisfiable dpll = new DPLLSatisfiable();
	public static void main(String[] args) {
		String KBU =   "((N_2_6 & N_3_6 & ~N_4_4 & ~N_4_5 & ~N_4_6) | (N_2_6 & ~N_3_6 & N_4_4 & ~N_4_5 & ~N_4_6) | (~N_2_6 & N_3_6 & N_4_4 & ~N_4_5 & ~N_4_6) | (N_2_6 & ~N_3_6 & ~N_4_4 & N_4_5 & ~N_4_6) | (~N_2_6 & N_3_6 & ~N_4_4 & N_4_5 & ~N_4_6) | (~N_2_6 & ~N_3_6 & N_4_4 & N_4_5 & ~N_4_6) | (N_2_6 & ~N_3_6 & ~N_4_4 & ~N_4_5 & N_4_6) | (~N_2_6 & N_3_6 & ~N_4_4 & ~N_4_5 & N_4_6) | (~N_2_6 & ~N_3_6 & N_4_4 & ~N_4_5 & N_4_6) | (~N_2_6 & ~N_3_6 & ~N_4_4 & N_4_5 & N_4_6))";
		String p = "N_0_6";
		String prove = KBU + " & ~" + p;
		boolean ans = displayDPLLSatisfiableStatus(prove);
		System.out.println(ans);
	}
	
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
