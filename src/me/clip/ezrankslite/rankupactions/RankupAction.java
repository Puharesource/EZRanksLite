package me.clip.ezrankslite.rankupactions;

public class RankupAction {

	private String executable;
	
	private RankupActionType type;
	
	public RankupAction(RankupActionType type, String executable) {
		this.setType(type);
		this.setExecutable(executable);
	}

	public String getExecutable() {
		return executable;
	}

	public void setExecutable(String executable) {
		this.executable = executable;
	}

	public RankupActionType getType() {
		return type;
	}

	public void setType(RankupActionType type) {
		this.type = type;
	}
}
