package me.clip.ezrankslite.rankdata;

public class LastRank {

	private String rank;
	
	private String prefix;
	
	public LastRank(String rank, String prefix) {
		this.rank = rank;
		this.prefix = prefix;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
