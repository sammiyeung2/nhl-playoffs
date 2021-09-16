import java.util.Iterator;

/**
 * This class represents the NHL Playoffs. Each NHL Playoff has a linked binary tree and a hockey series array representing the standings.
 * @author sammi
 *
 */
public class Playoffs {
	
	// represents the binary tree for playoffs
	private LinkedBinaryTree<String> tree;
	// an array of hockeyseries type 
	private HockeySeries[] standings;

	
	/**
	 * Constructor reads in the team names from a given file then fills in the hockeyseries array with those team names and builds a binary tree.
	 */
	public Playoffs() {

		// make a string array to represent all 4 rounds of the playoffs
		String[] array = new String[31];
		
		// for the first 15 slots, put a "TBD #" placeholder for the winners of each round
		for (int i = 0; i < 15; i++) {
			array[i] = "TBD " + i;
		}
		
		// read in the file of team names
		MyFileReader teamsFile = new MyFileReader("teams.txt");
		
		// fill up the rest of the slots in the string array with the team names
		for (int i = 15; i < 31; i++) {
			// keep reading the file line by line
			array[i] = teamsFile.readString();
		}
		
		
		/**
		 * Initialize the standings table with 15 slots (there will be exactly 15 series in the
			playoffs – 8 in round 1, 4 in round 2, 2 in round 3, and 1 in round 4).
				 Take every 2 teams from the array you just made and group them
				together into a series. The order is important here so ensure you take
				consecutive pairs the way they were written in the teams.txt file).
				 For each pair, create a HockeySeries object with those two team names
				and 0 for each of their default number of wins
		 */
		// initialize the standings table with 15 slots, with each slot representing each of the 15 series in the playoffs
		standings = new HockeySeries[15];
		// skip over TBD 0 when reading in the team name pairings
		int j = 1;
		// for each series in the first round, 
		for (int i = 0; i < 8; i++) {
			// group every 2 teams from the input file and put them into the series
			standings[i] = new HockeySeries(array[j+14], array[j+15], 0, 0);
			j += 2;
		}
		
		// start building a tree with the data from the array
		TreeBuilder<String> buildingTree = new TreeBuilder<String>(array);
		// initialize this class' tree
		tree = buildingTree.getTree();
		
		// close the file input reader
		teamsFile.close();
	}
	
	/**
	 * Accessor method to get the linked binary tree of type String
	 * @return the linked binary tree
	 */
	public LinkedBinaryTree<String> getTree() {
		return tree;
	}

	/**
	 * Accessor method to get the standings array of type HockeySeries
	 * @return the standings of the teams 
	 */
	public HockeySeries[] getStandings() {
		return standings;
	}
	
	/**
	 * Accessor method to update the standings of the teams in the round.
	 * @param teamA a string representing the first team name
	 * @param teamB a string representing the second team name
	 * @param winsA an integer representing the number of wins the first team earned
	 * @param winsB an integer representing the number of wins the second team earned
	 * @return a team name if they achieve 4 wins OR null if neither has reached 4 wins yet or the series can't be found in the array
	 */
	public String updateStandings(String teamA,String teamB,int winsA,int winsB) {
		
		/**
		 * Search the standings array to look for the series that corresponds to the given
			team names. Once you find the correct HockeySeries object for those teams, you
			can use the incrementWins() method on that object to update both teams' wins
			(typically one will be a 1 and one will be a 0).
		 */
		// for each slot in the standings array
		for (int i = 0; i < standings.length; i++) {
			// if a series exists at that slot (aka if that slot represents a series in the round)
			if (standings[i] != null) {
				// if that series is the correct series we are searching for
				if ((standings[i].getTeamA().equals(teamA)) && (standings[i].getTeamB().equals(teamB))) {
					// increment the wins by the appropriate number
					standings[i].incrementWins(winsA, winsB);
					
					// if the first team has reached 4 wins, return its name
					if (standings[i].getTeamAWins() == 4) {
						return standings[i].getTeamA();
					}
					// if the second team has reached 4 wins, return its name
					else if (standings[i].getTeamBWins() == 4) {
						return standings[i].getTeamB();
					}
					// if neither of the teams have reached 4 wins, return null
					else {
						return null;
					}
				}
			}
			
		}
		
		// if the series couldn't be found in the array, return null
		return null;
	}
	
	/**
	 * Setter method that reads the round's game scores from the appropriate text file
	 * and updates the standings of the team and the elements of the binary tree.
	 * @param i the corresponding round (1, 2, 3, or 4)
	 */
	public void updateRound(int i) {
		
		// load in the appropriate scores text file that corresponds to the given round
		MyFileReader scoresFile = new MyFileReader("scores" + i + ".txt");
		
		// while there are lines in the file to read (aka while it's not the end of the file)
		while (scoresFile.endOfFile() == false) {
			
			/**
			 * You may want to use the String's split() method to convert the String of one line
				into an array of 4 Strings based on the comma delimiter
			 */
			// read each line from the file (which represents a game in the round)
			String fileString = scoresFile.readString();
			// covert the line into an array of 4 strings using a comma delimiter
			String[] fileValues = fileString.split(",");
			// assign variable names to make it easier to remember and access each entry in the array
			String teamA = fileValues[0];
			String teamB = fileValues[1];
			// convert the strings representing the scores so that they're integers
			int scoreA = Integer.parseInt(fileValues[2]);
			int scoreB = Integer.parseInt(fileValues[3]);
			
			
			// set the default value of the returnedString to null
			String returnedString = null;
			
			// if team A won against team B, add one to their series score
			if (scoreA > scoreB) {
				returnedString = updateStandings(teamA,teamB, 1, 0);			
				
			}
			// otherwise, if team B won against team A, add one to their series score
			else if (scoreB > scoreA) {
				returnedString = updateStandings(teamA,teamB, 0, 1);
			
			}
			
			// if any of the teams reached 4 wins (and thus a team name is returned from the updateStandings method)
			if (returnedString != null) {
				// find the parent node of the two teams
				BinaryTreeNode<String> parent = findParent(teamA, teamB);
				// if the parent node is not null
				if (parent != null) {
					// update the parent node to hold the name of the team that won the series
					parent.setData(returnedString);
				}
					
			}
			
		}
		
		// close the file input
		scoresFile.close();
		
	}

	/**
	 * Accessor method to find the parent node given the elements of its two children nodes.
	 * @param teamA the element in the left child node
	 * @param teamB the element in the right child node
	 * @return
	 */
	public BinaryTreeNode<String> findParent(String teamA, String teamB) {

		// create a new queue that holds binary tree nodes of type String to start the level-order traversal
		LinkedQueue<BinaryTreeNode<String>> queue = new LinkedQueue<BinaryTreeNode<String>>();
		// enqueue the root node to enter the while loop below
		queue.enqueue(tree.getRoot());
		
		// while the queue is not empty (aka there are still nodes that need to be visited or the parent node has not been found yet)
		while (!queue.isEmpty()) {
			
			// make a new binary tree node from the dequeued element from the queue
			BinaryTreeNode<String> node = queue.dequeue();
			
			// if there's a left child to the node being visited, add it to the queue of nodes to visit
			if (node.getLeft() != null) {
				queue.enqueue(node.getLeft());
			}
			// if there's a right child to the node being visited, add it to the queue of nodes to visit
			if (node.getRight() != null) {
				queue.enqueue(node.getRight());
			}
			// if the node being visited has both children
			if (node.getLeft() != null && node.getRight() != null) {
				// visit its left node
				String visitLeftTeam = node.getLeft().getData();
				// visit its right node
				String visitRightTeam = node.getRight().getData();
				// if the string elements in both child nodes match the names of the children whose parent we are searching for
				if (visitLeftTeam.equals(teamA) && visitRightTeam.equals(teamB)) {
					// return the parent node
					return node;
				}
			}
		}
		
		// otherwise return null if the parent can't be found and/or doesn't exist
		return null;
		
	}
	

	
	/**
	 * This method adds the new series to the standings array before a new round begins. It does this using an iterator
	 * from the tree and skipping over the nodes that are still unknown until it gets to the nodes from the new round.
	 * It then takes two teams at a time from the iterator and creates a new series in the standings array for those
	 * two teams. The series standings (number of wins for each team) are set to 0 by default. 
	 */
	public void addNewStandings (int numSkips, int sIndex, int eIndex) {
		Iterator<String> iter = tree.iteratorLevelOrder();
		int i;
		String team1, team2;
		for (i = 0; i < numSkips; i++) {
			iter.next();
		}
		for (i = sIndex; i <= eIndex; i++) {
			team1 = iter.next();
			team2 = iter.next();
			standings[i] = new HockeySeries(team1, team2, 0, 0);
		}
	}
	
	/**
	 * This method simply prints out the standings table in a cleanly formatted table structure.
	 */
	public void printStandings () {
		String str;
		for (int k = 0; k < standings.length; k++) {
			if (standings[k] != null) {
				str = String.format("%-15s\t%-15s\t%3d-%d", standings[k].getTeamA(), standings[k].getTeamB(), standings[k].getTeamAWins(), standings[k].getTeamBWins());
				System.out.println(str);
			}
		}
	}
	
	
	public static void main(String[] args) {
		Playoffs pl = new Playoffs();
		pl.updateRound(1);
		pl.printStandings();

		// Uncomment each pair of lines when you are ready to run the subsequent rounds. 
		
		//pl.addNewStandings(7, 8, 11); // Ensure you execute this line before calling updateRound(2).
		//pl.updateRound(2);

		
		//pl.addNewStandings(3, 12, 13); // Ensure you execute this line before calling updateRound(3).
		//pl.updateRound(3);

		
		//pl.addNewStandings(1, 14, 14); // Ensure you execute this line before calling updateRound(4).
		//pl.updateRound(4);
	}

}
