
public class Champion {
	private int id;
	private String name;
	private double avgGoldShare;
	private int numGamesPlayed;
	private double totalAvgGoldShare;
	private String role;
	private int wins = 0;
	public Champion(int id, String name, String role) {
		super();
		this.id = id;
		this.name = name;
		this.role = role;
		this.totalAvgGoldShare = 0;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getAvgGoldShare() {
		return avgGoldShare;
	}
	public void setAvgGoldShare(double avgGoldShare) {
		this.avgGoldShare = avgGoldShare;
	}
	public int getNumGamesPlayed() {
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
		numGamesPlayed++;
		totalAvgGoldShare += goldShare;
	}
	
	public double calcAvgGoldShare() {
		this.avgGoldShare = this.totalAvgGoldShare/this.numGamesPlayed;
		return avgGoldShare;
	}
	
	@Override
    public boolean equals(Object o){
        if(o instanceof Champion){
             Champion p = (Champion) o;
             return this.name.equals(p.getName());
        } else
             return false;
    }
	public void addWin() {
		wins++;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}
	
	
	
}
