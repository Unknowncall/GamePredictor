import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
					System.out.println("The probability that " + teamOne + " will win against " + teamTwo + " is: " + df.format(prob) + "%.");
				}
				
				break;
			case "2": // Stretch 1
				
				break;
			case "3": // Stretch 2
				
				break;
			case "4": // Stretch 3

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
	 * What are the odds that teamOne will beat teamTwo based on our historical data?
	 * @param teamOne
	 * @param teamTwo
	 * @return the probility of the chance team one will win. If the team has never played one another, it will return NaN.
	 */
	public double winProbability(int teamOne, int teamTwo) {
		/*
		 * P(teamOne = T | teamTwo = F) = P(teamTwo = F | teamOne = T) * P(teamOne = T) / P(teamTwo = F)
		 * P(teamTwo = F | teamOne = T) How many times teamTwo has lost when they were facing teamOne
		 * P(teamOne = T) How many times team one has won in total/total games.
		 * P(teamTwo = F) How many times has team two lost in total/total games.
		 */
		
		String firstSQL = "SELECT * FROM `game` WHERE (team_id_1 = " + teamOne + " AND team_id_2 = " + teamTwo + ") OR (team_id_1 = " + teamTwo + " AND team_id_2 = " + teamOne + ")";
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
		
		double numOne = wins/(totalGamesPlayed + 0.0);
		
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
		
		double numTwo = wins/(totalGamesPlayed + 0.0);
		
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
		
		double numThree = loses/(totalGamesPlayed + 0.0);
		return numOne * numTwo / numThree;
	}

}
