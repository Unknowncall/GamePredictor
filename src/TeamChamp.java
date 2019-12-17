
public class TeamChamp {
	int teamID;
	int champID;
	int numGames = 0;
	int wins = 0;
	
	
	public TeamChamp(int teamID, int champID) {
		super();
		this.teamID = teamID;
		this.champID = champID;
	}
	public int getTeamID() {
		return teamID;
	}
	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}
	public int getChampID() {
		return champID;
	}
	public void setChampID(int champID) {
		this.champID = champID;
	}
	public int getNumGames() {
		return numGames;
	}
	public void setNumGames(int numGames) {
		this.numGames = numGames;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public void addLoss() {
		numGames++;
	}
	public void addWin() {
		numGames++;
		wins++;
	}
	
	@Override
    public boolean equals(Object o){
        if(o instanceof TeamChamp){
            TeamChamp p = (TeamChamp) o;
             return (this.teamID == (p.getTeamID())  && this.champID == (p.getChampID()));
        } else
             return false;
    }
	
}
