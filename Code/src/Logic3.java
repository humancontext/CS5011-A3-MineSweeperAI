/**
 * This is the game method for Logic1 with RGS_SPS agent.
 * @author 170024030
 *
 */
public class Logic3 {
	/**
	 * The main method
	 * @param args indicate the chosen nettle world.
	 */
	public static void main(String[] args) {
		/*
		 * all map are stored in an two-dimensional arrayList
		 * the following code get the wanted map from the arguments. 
		 */
		try {
			int m = 0;
			int n = Integer.parseInt(args[1]) - 1;
			switch (args[0]) {
			case "easy":
				m = 0;
				break;
			case "medium":
				m = 1;
				break;
			case "hard":
				m = 2;
				break;
			case "master":
				m = 3;
				break;
			default:
				System.err.println("Invalid level!");
				System.exit(-1);
			}
			Map maps = new Map();
			//this is the map to play with.
			int[][] map = maps.nworld.get(m).get(n);
			//instantiate the RGS_SPS agent
			Agent_RGS_SPS_DLS agent = new Agent_RGS_SPS_DLS(map);
			long start = System.currentTimeMillis();
			//start from upper left corner
			agent.probe(0, 0);
			agent.showMap();
			//loop until all cells are marked or probed
			while(!agent.unknown.isEmpty()) {
				/*
				 * if SPS doesn's work resort to DLS
				 * if DLS doesn's work resort to RGS
				 */
				if (!agent.sps() && !agent.dls()) {
					agent.rgs();
				}
				if (!agent.isSafe) {
					break;
				}
			}
			long end = System.currentTimeMillis();
			if (agent.isSafe) {
				System.out.println("Time(ms): " + (end - start));
				System.out.println("Number of random guesses: " + agent.rgsCount);
				System.out.println("Number of SPS used: " + agent.spsCount);
				System.out.println("Number of DLS used: " + agent.dlsCount);
			}
			else {
				System.out.println("Sorry you lose");
			}
		}
		catch (IndexOutOfBoundsException e) {
			System.err.println("Usage: java - jar Logic1_1.jar <level> <map_number>");
		}
		

	}
}
