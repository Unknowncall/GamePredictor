import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {

		Driver driver = null;
		try {
			driver = new Driver();
			driver.openConnection();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to our AI project on predicting esports games.");
		DecimalFormat df = new DecimalFormat("00.00");
		boolean run = true;

		while (run) {
			System.out.println("Menu.\n1. Game Guesser \n2. Stretch 1\n3. Stretch 2\n4. Stretch 3\n5. Exit");

			String input = scanner.next();

			switch (input) {
			case "1": // mvp
				System.out.println("Please input the team id for team one (Must be an integer): ");
				int teamOne = scanner.nextInt();
				System.out.println("Please input the team id for team two (Must be an integer): ");
				int teamTwo = scanner.nextInt();

				double prob = driver.winProbability(teamOne, teamTwo);

				if (Double.isNaN(prob)) {
					System.out.println("We do not have enough data to predict who will win this game.");
				} else {
					prob = prob * 100;
					System.out.println("The probability that " + teamOne + " will win against " + teamTwo + " is: "
							+ df.format(prob) + "%.");
				}

				break;
			case "2":
				System.out.println("Please input the team id for team one (Must be an integer): ");
				int teamOne1 = scanner.nextInt();
				System.out.println("Please input the team id for team two (Must be an integer): ");
				int teamTwo1 = scanner.nextInt();

				HashMap<Integer, Double> contrib = driver.playerContribution(teamOne1, teamTwo1);

				if (contrib == null) {
					System.out.println(
							"These teams have never played each other before. We do not have data to complete this task.");
				} else {
					System.out.println("This is the estimated contribution for team: " + teamOne1 + ".");
					for (int key : contrib.keySet()) {
						String sql = "SELECT player_name FROM player WHERE player_id = '" + key + "'";
						try {
							ResultSet set = driver.connection.createStatement().executeQuery(sql);
							set.next();
							System.out.println(
									set.getString("player_name") + ": " + df.format(contrib.get(key) * 100) + "%.");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
				break;
			case "3": // Stretch 2
				System.out.println("Please enter a champion's name: ");
				String champName = scanner.next();

				double conf = driver.OneR(champName);

				if (Double.isNaN(conf)) {
					System.out.println("We do not have enough data to predict who will win this game.");
				} else {
					conf = conf * 100;
					System.out.println(
							"According to One Rule this champions confidence rating is " + df.format(conf) + "%.");
				}
				break;
			case "4": // Stretch 3
				System.out.println("Pleaes enter a team id: ");
				int teamId = scanner.nextInt();

				HashMap<Integer, Double> probChamp = driver.winProbWithChampion(teamId);
				probChamp = driver.sortByValue(probChamp);

				int counter = 0;
				for (int champ : probChamp.keySet()) {
					if (counter >= 5) {
						break;
					}
					double value = probChamp.get(champ) * 100;
					System.out.println("According to bayesin inference, this role should be banned with a: " + champ
							+ "-" + df.format(value) + "%");
					counter++;
				}

				break;
			case "5":
				run = false;
				break;
			default:
				System.out.println("That was not a valid input. Please input, 1, 2, 3, 4, or 5.");
				break;
			}

		}

		System.out.println("Exiting...");
		scanner.close();
		try {
			driver.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, Double> sortByValue(HashMap<Integer, Double> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}

		});

		HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
		for (Map.Entry<Integer, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	private Connection connection;
	private String host, database, username, password;
	private int port;

	public void openConnection() throws SQLException, ClassNotFoundException {

		long start = System.currentTimeMillis();
		System.out.println("Opening connection...");
		host = "34.238.181.127";
		port = 3306;
		database = "ai-332";
		username = "ai-332";
		password = "maresso_brain";

		if (connection != null && !connection.isClosed()) {
			return;
		}

		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
		}

		System.out.println("Connection opened. Time taken: " + (System.currentTimeMillis() - start) + " m/s");

	}

	/**
	 * What are the odds that teamOne will beat teamTwo based on our historical
	 * data? Bayesian
	 * 
	 * @param teamOne
	 * @param teamTwo
	 * @return the probility of the chance team one will win. If the team has never
	 *         played one another, it will return NaN.
	 */
	public double winProbability(int teamOne, int teamTwo) {
		/*
		 * P(teamOne = T | teamTwo = F) = P(teamTwo = F | teamOne = T) * P(teamOne = T)
		 * / P(teamTwo = F) P(teamTwo = F | teamOne = T) How many times teamTwo has lost
		 * when they were facing teamOne P(teamOne = T) How many times team one has won
		 * in total/total games. P(teamTwo = F) How many times has team two lost in
		 * total/total games.
		 */

		String firstSQL = "SELECT * FROM `game` WHERE (team_id_1 = " + teamOne + " AND team_id_2 = " + teamTwo
				+ ") OR (team_id_1 = " + teamTwo + " AND team_id_2 = " + teamOne + ")";
		int totalGamesPlayed = 0;
		int wins = 0;
		try {
			ResultSet set = connection.createStatement().executeQuery(firstSQL);
			while (set.next()) {
				totalGamesPlayed++;
				if (set.getInt("result") == teamOne) {
					wins++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		double numOne = wins / (totalGamesPlayed + 0.0);

		String secondSQL = "SELECT * FROM `game` WHERE team_id_1 = " + teamOne + " OR team_id_2 = " + teamOne;

		totalGamesPlayed = 0;
		wins = 0;

		try {
			ResultSet set = connection.createStatement().executeQuery(secondSQL);
			while (set.next()) {
				totalGamesPlayed++;
				if (set.getInt("result") == teamOne) {
					wins++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		double numTwo = wins / (totalGamesPlayed + 0.0);

		String thirdSQL = "SELECT * FROM `game` WHERE team_id_1 = " + teamTwo + " or team_id_2 = " + teamTwo;

		totalGamesPlayed = 0;
		int loses = 0;

		try {
			ResultSet set = connection.createStatement().executeQuery(thirdSQL);
			while (set.next()) {
				totalGamesPlayed++;
				if (set.getInt("result") != teamTwo) {
					loses++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		double numThree = loses / (totalGamesPlayed + 0.0);
		return numOne * numTwo / numThree;
	}

	// MVP calculated by the win percent and avg gold share.
	public HashMap<Integer, Double> playerContribution(int teamOne, int teamTwo) {

		if (Double.isNaN(winProbability(teamOne, teamTwo))) {
			return null;
		}

		String fourthSQL = "SELECT * FROM `player` WHERE team_id = " + teamOne
				+ " ORDER BY `avg_gold_share` DESC LIMIT 5";

		try {
			ResultSet set = connection.createStatement().executeQuery(fourthSQL);
			HashMap<Integer, Double> contribs = new HashMap<Integer, Double>();

			while (set.next()) {
				contribs.put(set.getInt("player_id"), set.getDouble("avg_gold_share"));
			}

			HashMap<Integer, Double> finalContribs = new HashMap<Integer, Double>();
			double totalAvgGoldShare = 0.0;

			for (Integer key : contribs.keySet()) {
				totalAvgGoldShare += contribs.get(key);
			}

			for (Integer key : contribs.keySet()) {
				finalContribs.put(key, (contribs.get(key) / totalAvgGoldShare));
			}

			return finalContribs;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public double OneR(String champName) {

		double conf = 0;
		int numGames = 0;
		int wins = 0;

		String query2 = "SELECT num_games FROM `champion` WHERE champion_name = '" + champName + "'";
		try {
			ResultSet set = connection.createStatement().executeQuery(query2);
			set.next();
			numGames = set.getInt("num_games");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query3 = "SELECT wins FROM `champion` WHERE champion_name = '" + champName + "'";
		try {
			ResultSet set = connection.createStatement().executeQuery(query3);
			set.next();
			wins = set.getInt("wins");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		conf = (wins / (numGames + 0.0));
		return conf;

	}

	// When the team wins and the champion is on the team.
	/*
	 * P(team = T | champ = T) = P(champ = T | team = T) * P(team = T) / P(champ =
	 * T)
	 */
	public HashMap<Integer, Double> winProbWithChampion(int teamId) {

		ArrayList<Integer> champs = new ArrayList<Integer>();

		String sql = "SELECt * FROM `team_champions` WHERE team_id = " + teamId;

		try {
			ResultSet set = connection.createStatement().executeQuery(sql);
			while (set.next()) {
				champs.add(set.getInt("champion_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		HashMap<Integer, Double> returnValues = new HashMap<Integer, Double>();

		for (int champId : champs) {
			// P(champ = T | team = T)
			String firstSql = "SELECT * FROM `team_champions` WHERE team_id = " + teamId + " AND champion_id = "
					+ champId;
			double first = 0.0;

			try {

				int totalWins = 0;
				int totalGames = 0;

				ResultSet set = connection.createStatement().executeQuery(firstSql);
				while (set.next()) {
					totalWins += set.getInt("wins");
					totalGames += set.getInt("num_games");
				}

				first = totalWins / (totalGames + 0.0);

			} catch (SQLException e) {
				e.printStackTrace();
			}

			// P(team = T)

			String secondSql = "SELECT * FROM `team_champions` WHERE team_id = " + teamId;
			double second = 0.0;

			try {

				int totalWins = 0;
				int totalGames = 0;

				ResultSet set = connection.createStatement().executeQuery(secondSql);
				while (set.next()) {
					totalWins += set.getInt("wins");
					totalGames += set.getInt("num_games");
				}

				second = totalWins / (totalGames + 0.0);

			} catch (SQLException e) {
				e.printStackTrace();
			}

			// P(champ = T)

			String thirdSql = "SELECT * FROM `team_champions` WHERE champion_id = " + champId;
			double third = 0.0;

			try {

				int totalWins = 0;
				int totalGames = 0;

				ResultSet set = connection.createStatement().executeQuery(thirdSql);
				while (set.next()) {
					totalWins += set.getInt("wins");
					totalGames += set.getInt("num_games");
				}

				third = totalWins / (totalGames + 0.0);

			} catch (SQLException e) {
				e.printStackTrace();
			}

			returnValues.put(champId, (first * second) / third);

		}

		return returnValues;

	}

}
