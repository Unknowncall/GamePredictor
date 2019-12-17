
public class Player {
	private String name;
	private int id;
	private double avgGoldShare;
	private double numGamesPlayed;
	private double totalAvgGoldShare;
	private double gamesWon;
	private int teamid;
	public Player(String name, int id, double totalAvgGoldShare) {
		super();
		this.name = name;
		this.id = id;
		this.totalAvgGoldShare = totalAvgGoldShare;
		this.numGamesPlayed = 1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getAvgGoldShare() {
		return avgGoldShare;
	}
	public void setAvgGoldShare(double avgGoldShare) {
		this.avgGoldShare = avgGoldShare;
	}
	public double getNumGamesPlayed() {
		return numGamesPlayed;
	}
	public void setNumGamesPlayed(int numGamesPlayed) {
		this.numGamesPlayed = numGamesPlayed;
	}
	public double getTotalAvgGoldShare() {
		return totalAvgGoldShare;
	}
	public void setTotalAvgGoldShare(double totalAvgGoldShare) {
		this.totalAvgGoldShare = totalAvgGoldShare;
	}
	public void addGame(double goldShare) {
		++numGamesPlayed;
		totalAvgGoldShare+=goldShare;
	}
	public void calculateAvgGoldShare() {
		avgGoldShare = totalAvgGoldShare/numGamesPlayed;
	}
	
	public double getGamesWon() {
		return gamesWon;
	}
	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
	}
	public void gameWon() {
		this.gamesWon++;
	}
	public double winRate() {
		return gamesWon/numGamesPlayed;
	}
	@Override
    public boolean equals(Object o){
        if(o instanceof Player){
             Player p = (Player) o;
             return this.name.equals(p.getName());
        } else
             return false;
    }
	public int getTeamid() {
		return teamid;
	}
	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}
	public void setNumGamesPlayed(double numGamesPlayed) {
		this.numGamesPlayed = numGamesPlayed;
	}
	public void setGamesWon(double gamesWon) {
		this.gamesWon = gamesWon;
	}
	
}
