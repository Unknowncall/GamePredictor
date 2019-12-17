import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class csvGenerator {
	ArrayList<Player> players = new ArrayList<Player>();
	ArrayList<Team> teams = new ArrayList<Team>();
	ArrayList<Champion> champions = new ArrayList<Champion>();
	ArrayList<Game> games = new ArrayList<Game>();
	ArrayList<TeamChamp> teamChamps = new ArrayList<TeamChamp>();
	int playerID = 1;
	int teamID = 1;
	int gameID = 1;
	int champID;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		csvGenerator obj = new csvGenerator();
		
		obj.generateTeams("2016 match data.csv");
		obj.generateTeams("2017 match data.csv");
		obj.generateTeams("2018 spring match data.csv");
		obj.generateTeams("2018 summer match data.csv");
		obj.generateTeams("2018 worlds match data.csv");
		obj.generateTeams("2019 spring match data.csv");
		obj.generateTeams("2019 summer match data.csv");
		obj.generateTeams("2019 worlds match data.csv");
		
		obj.teamsCSV();
		
		
		obj.generatePlayers("2016 match data.csv");
		obj.generatePlayers("2017 match data.csv");
		obj.generatePlayers("2018 spring match data.csv");
		obj.generatePlayers("2018 summer match data.csv");
		obj.generatePlayers("2018 worlds match data.csv");
		obj.generatePlayers("2019 spring match data.csv");
		obj.generatePlayers("2019 summer match data.csv");
		obj.generatePlayers("2019 worlds match data.csv");
		for(int i = 0; i < obj.players.size(); i++) {
			obj.players.get(i).calculateAvgGoldShare();
		}
		obj.playersCSV();
		
		obj.generateGames("2016 match data.csv");
		obj.generateGames("2017 match data.csv");
		obj.generateGames("2018 spring match data.csv");
		obj.generateGames("2018 summer match data.csv");
		obj.generateGames("2018 worlds match data.csv");
		obj.generateGames("2019 spring match data.csv");
		obj.generateGames("2019 summer match data.csv");
		obj.generateGames("2019 worlds match data.csv");
		
		obj.gamesCSV();
		
		
		obj.generateChampions("champion_list.csv");
		obj.populateChampions("2016 match data.csv");
		obj.populateChampions("2017 match data.csv");
		obj.populateChampions("2018 spring match data.csv");
		obj.populateChampions("2018 summer match data.csv");
		obj.populateChampions("2018 worlds match data.csv");
		obj.populateChampions("2019 spring match data.csv");
		obj.populateChampions("2019 summer match data.csv");
		obj.populateChampions("2019 worlds match data.csv");
		
		obj.championsCSV();
		

		
		obj.generateTeamChamps("2016 match data.csv");
		obj.generateTeamChamps("2017 match data.csv");
		obj.generateTeamChamps("2018 spring match data.csv");
		obj.generateTeamChamps("2018 summer match data.csv");
		obj.generateTeamChamps("2018 worlds match data.csv");
		obj.generateTeamChamps("2019 spring match data.csv");
		obj.generateTeamChamps("2019 summer match data.csv");
		obj.generateTeamChamps("2019 worlds match data.csv");
		
		obj.teamChampsCSV();
	}
	
	public void generatePlayers(String filename) throws IOException, InterruptedException {
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
    	String line = br.readLine();
    	String[] parts = line.split(",");
    	int nameIndex = -1;
    	int goldIndex = -1;
    	int resultIndex = -1;
    	int teamIndex = -1;
    	for(int i = 0; i < parts.length; i++) {
    		if(parts[i].equalsIgnoreCase("player")) {
    			nameIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("earnedgoldshare")) {
    			goldIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("result")) {
    			resultIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("team")) {
    			teamIndex = i;
    		}
    	}
    	if(nameIndex == -1 || goldIndex == -1 || resultIndex == -1) {
    		System.out.println("File error. Name, result or Gold Share column not found.");
    		System.exit(1);
    	}
    	 while((line = br.readLine()) != null) {
    		 parts = line.split(",");
    		 String name = parts[nameIndex];
    		 if(name.equalsIgnoreCase("Team") || name.isEmpty() || parts[0].isEmpty()){
    			 continue;
    		 }
    		 String goldShare = parts[goldIndex];
    		 if(goldShare.isEmpty() || goldShare == null || goldShare.equals(" ")) {
    			 continue;
    		 }
    		 String result = parts[resultIndex];
    		 int teamId = -1;
    		 for(int i = 0; i < teams.size(); i++) {
    			 if(teams.get(i).getName().equals(parts[teamIndex])) {
    				 teamId = i;
    			 }
    		 }
    		 Player player = new Player(name, playerID, Double.parseDouble(goldShare)); 
    		 if(result.equals("1")) {
    				 player.gameWon();
    		 }
    		 if(!players.contains(player)) { 
    			player.setTeamid(teamId);
    			 players.add(player);
    			 ++playerID;
    			
    		 }
    		 else {
    			 players.get(players.indexOf(player)).addGame(Double.parseDouble(goldShare));
    			 if(result.equals("1")) {
    				 players.get(players.indexOf(player)).gameWon();
    				 players.get(players.indexOf(player)).setTeamid(teamId);
    			}
    		 }
    	 }
    	 
    	 System.out.println(filename + " is done!");
    	 br.close();
	}
	
	public void playersCSV() throws IOException {
		FileWriter writer = new FileWriter("players.csv");
		writer.append("player_id");
		writer.append(',');
		writer.append("player_name");
		writer.append(',');
		writer.append("avg_gold_share");
		writer.append(',');
		writer.append("num_games");
		writer.append(',');
		writer.append("win_percent");
		writer.append(',');
		writer.append("team_id");
		writer.append('\n');
		for(int i = 0; i<players.size(); i++) {
			writer.append(Integer.toString(players.get(i).getId()));
			writer.append(',');
			writer.append(players.get(i).getName());
			writer.append(',');
			writer.append(Double.toString(players.get(i).getAvgGoldShare()));
			writer.append(',');
			writer.append(Double.toString(players.get(i).getNumGamesPlayed()));
			writer.append(',');
			writer.append(Double.toString(players.get(i).winRate()));
			writer.append(',');
			writer.append(Integer.toString(players.get(i).getTeamid()));
			writer.append('\n');
			
		}
			writer.flush();
			writer.close();
	}
	
	public void generateTeams(String filename) throws IOException, InterruptedException {
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
    	String line = br.readLine();
    	String[] parts = line.split(",");
    	int nameIndex = -1;
    	for(int i = 0; i < parts.length; i++) {
    		if(parts[i].equalsIgnoreCase("team")) {
    			nameIndex = i;
    		}
    	}
    	if(nameIndex == -1) {
    		System.out.println("File error. Team name column not found.");
    		System.exit(1);
    	}
    	 while((line = br.readLine()) != null) {
    		 parts = line.split(",");
    		 String name = parts[nameIndex];
    		 Team team = new Team(teamID, name);
    		 if(!teams.contains(team)) {
    			 teams.add(team);
    			 ++teamID;
    		 }
    	 }
    	 
    	 System.out.println(filename + " is done!");
    	 br.close();
	}
	public void teamsCSV() throws IOException {
		FileWriter writer = new FileWriter("teams.csv");
		writer.append("team_id");
		writer.append(',');
		writer.append("team_name");
		writer.append('\n');
		for(int i = 0; i<teams.size(); i++) {
			writer.append(Integer.toString(teams.get(i).getId()));
			writer.append(',');
			writer.append(teams.get(i).getName());			
			writer.append('\n');
			
		}
			writer.flush();
			writer.close();
	}
	
	public void generateChampions(String filename) throws IOException, InterruptedException {
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
    	String line = br.readLine();
    	String[] parts = line.split(",");
    	
    	 while((line = br.readLine()) != null) {
    		 parts = line.split(",");
    		 
    		  
    		 champID = Integer.parseInt(parts[0]) + 1;
    		 String name = parts[1];
    		 Champion champ = new Champion(champID, name, "general");
    		 champions.add(champ);
    	 }
    	 
    	 System.out.println(filename + " is done!");
    	 br.close();
	}
	
	public void populateChampions(String filename) throws NumberFormatException, IOException {
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
    	String line = br.readLine();
    	String[] parts = line.split(",");
    	int nameIndex = -1;
    	int goldIndex = -1;
    	int positionIndex = -1;
    	int resultIndex = -1;
    	for(int i = 0; i < parts.length; i++) {
    		if(parts[i].equalsIgnoreCase("champion")) {
    			nameIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("earnedgoldshare")) {
    			goldIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("position")) {
    			positionIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("result")) {
    			resultIndex = i;
    		}
    	}
    	 while((line = br.readLine()) != null) {
    		 parts = line.split(",");
    		 String champ = parts[nameIndex];
    		 String gold = parts[goldIndex];
    		 String role = parts[positionIndex];
    		 
    		 if(gold.isEmpty() || gold == null || gold.equals(" ")) {
    			 continue;
    		 }
    		 int champid = -1;
    		 boolean newChamp = true;
    		 for(int i = 0; i < champions.size(); i++) {
    			if(champions.get(i).getName().equalsIgnoreCase(champ)  && (champions.get(i).getRole().equals("general") || champions.get(i).getRole().equals(role))) {
    				champions.get(i).addGame(Double.parseDouble(gold));
    				champid = champions.get(i).getId();
    				
    				if(parts[resultIndex].equals("1")) {
    					champions.get(i).addWin();
    				}
    			}
    			if(champions.get(i).getName().equals(champ) && champions.get(i).getRole().equals(role)) {
    				newChamp = false;
    			}
    			 
    		 }
    		 
    		 if(newChamp) {
    			 Champion newChampion = new Champion(champid, champ, role);
    			 newChampion.addGame(Double.parseDouble(gold));

 				if(parts[resultIndex].equals("1")) {
 					newChampion.addWin();
 				}
    			 champions.add(newChampion);
    		 }
    		
    	 }
    	 
    	 System.out.println(filename + " is done!");
    	 br.close();
	}
	
	public void championsCSV() throws IOException {
		FileWriter writer = new FileWriter("champs.csv");
		writer.append("champ_id");
		writer.append(',');
		writer.append("champ_name");
		writer.append(',');
		writer.append("goldShareAvg");
		writer.append(',');
		writer.append("role");
		writer.append(',');
		writer.append("num_games");
		writer.append(',');
		writer.append("wins");
		writer.append('\n');
		for(int i = 0; i<champions.size(); i++) {
			writer.append(Integer.toString(champions.get(i).getId()));
			writer.append(',');
			writer.append(champions.get(i).getName());	
			writer.append(',');
			writer.append(Double.toString(champions.get(i).calcAvgGoldShare()));
			writer.append(',');
			writer.append(champions.get(i).getRole());
			writer.append(',');
			writer.append(Integer.toString(champions.get(i).getNumGamesPlayed()));
			writer.append(',');
			writer.append(Integer.toString(champions.get(i).getWins()));
			writer.append('\n');
			
		}
		writer.flush();
		writer.close();
}
	public void generateGames(String filename) throws IOException, InterruptedException {
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
    	String line = br.readLine();
    	String[] parts = line.split(",");
    	int nameIndex = -1;
    	int isTeamIndex = -1;
    	int resultIndex = -1;
    	for(int i = 0; i < parts.length; i++) {
    		if(parts[i].equalsIgnoreCase("team")) {
    			nameIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("player")) {
    			isTeamIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("result")) {
    			resultIndex = i;
    		}
    	}
    	if(nameIndex == -1) {
    		System.out.println("File error. Team name column not found.");
    		System.exit(1);
    	}
    	 while((line = br.readLine()) != null) {
    		 parts = line.split(",");
    		 String isTeam = parts[isTeamIndex];
    		 if(!isTeam.equalsIgnoreCase("Team")) {
    			 continue;
    		 }
    		 String line2 = br.readLine(); 
    		 String parts2[] = line2.split(",");
    		 String team1 = parts[nameIndex];
    		 String team2 = parts2[nameIndex];
    		 int team1id = -1;
    		 int team2id = -1;
    		 int result = Integer.parseInt(parts[resultIndex]);
    		 for(int i = 0; i < teams.size(); i++) {
    			 if(teams.get(i).getName().equals(team1)) {
    				 team1id = teams.get(i).getId();
    			 }
    			 if(teams.get(i).getName().equals(team2)) {
    				 team2id = teams.get(i).getId();
    			 }
    			 if(team1id != - 1 && team2id != -1) {
    				 break;
    			 }
    		 }
    		 if(result == 1) {
    			 result = team1id;
    		 }
    		 else {
    			 result = team2id;
    		 }
    		 Game game = new Game(team1id, team2id, gameID++                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     , result);
    		 games.add(game);
    		
    	 }
    	 
    	 System.out.println(filename + " is done!");
    	 br.close();
	}
	public void gamesCSV() throws IOException {
		FileWriter writer = new FileWriter("games.csv");
		writer.append("game_id");
		writer.append(',');
		writer.append("team1_id");
		writer.append(',');
		writer.append("team2_id");
		writer.append(',');
		writer.append("result");
		writer.append('\n');
		for(int i = 0; i<games.size(); i++) {
			writer.append(Integer.toString(games.get(i).getgID()));
			writer.append(',');
			writer.append(Integer.toString(games.get(i).getbSide()));	
			writer.append(',');
			writer.append(Integer.toString(games.get(i).getrSide()));
			writer.append(',');
			writer.append(Integer.toString(games.get(i).getResult()));
			writer.append('\n');
			
		}
			writer.flush();
			writer.close();
	}
	

	public void generateTeamChamps(String filename) throws IOException, InterruptedException {
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
    	String line = br.readLine();
    	String[] parts = line.split(",");
    	int nameIndex = -1;
    	int champIndex = -1;
    	int resultIndex = -1;
    	for(int i = 0; i < parts.length; i++) {
    		if(parts[i].equalsIgnoreCase("team")) {
    			nameIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("champion")) {
    			champIndex = i;
    		}
    		if(parts[i].equalsIgnoreCase("result")) {
    			resultIndex = i;
    		}
    	}
    	if(nameIndex == -1) {
    		System.out.println("File error. Team name column not found.");
    		System.exit(1);
    	}
    	 while((line = br.readLine()) != null) {
    		 parts = line.split(",");
    		 String isTeam = parts[champIndex];
    		 if(isTeam.equalsIgnoreCase(" ") || isTeam.equalsIgnoreCase(null) || isTeam.equalsIgnoreCase("")) {
    			 continue;
    		 }
    		 
    		 int result = Integer.parseInt(parts[resultIndex]);
    		 int champID = -1, teamID = -1;
    		 
    		 for(int i = 0; i < teams.size(); i++) {
    			 if(teams.get(i).getName().equals(parts[nameIndex])) {
    				 teamID = teams.get(i).getId();
    			 }
    		 }
    		 for(int i = 0; i < champions.size(); i++) {
    			 if(champions.get(i).getName().equals(parts[champIndex])) {
    				 champID = champions.get(i).getId();
    			 }
    		 }
    		 TeamChamp newChamp = new TeamChamp(teamID, champID);
    		 if(teamChamps.contains(newChamp)) {
    			 if(result == 1) {
    				teamChamps.get(teamChamps.indexOf(newChamp)).addWin();
    			 }
    			 else {
    				 teamChamps.get(teamChamps.indexOf(newChamp)).addLoss();
    			 }
    		 }
    		 else {
    			 if(result == 1) {
     				newChamp.addWin();
     			 }
     			 else {
     				 newChamp.addLoss();
     			 }
    			 
    			 teamChamps.add(newChamp);
    		 }    		
    	 }
    	 
    	 System.out.println(filename + " is done!");
    	 br.close();
	}
	
	public void teamChampsCSV() throws IOException {
		FileWriter writer = new FileWriter("teamChamps.csv");
		writer.append("team_id");
		writer.append(',');
		writer.append("champ_id");
		writer.append(',');
		writer.append("wins");
		writer.append(',');
		writer.append("num_games");
		writer.append('\n');
		for(int i = 0; i<teamChamps.size(); i++) {
			writer.append(Integer.toString(teamChamps.get(i).getTeamID()));
			writer.append(',');
			writer.append(Integer.toString(teamChamps.get(i).getChampID()));	
			writer.append(',');
			writer.append(Integer.toString(teamChamps.get(i).getWins()));
			writer.append(',');
			writer.append(Integer.toString(teamChamps.get(i).getNumGames()));
			writer.append('\n');
			
		}
			writer.flush();
			writer.close();
	}
	

}
