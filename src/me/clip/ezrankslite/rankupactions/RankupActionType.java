package me.clip.ezrankslite.rankupactions;

public enum RankupActionType {
	
	CONSOLE_COMMAND("[consolecommand]"), 
	PLAYER_COMMAND("[playercommand]"),
	MESSAGE("[message]"), 
	BROADCAST("[broadcast]"),
	JSON_MESSAGE("[jsonmessage]"), 
	JSON_BROADCAST("[jsonbroadcast]"), 
	ACTIONBAR_MESSAGE("[actionbarmessage]"), 
	ACTIONBAR_BROADCAST("[actionbarbroadcast]"),
	ADD_GROUP("[addgroup]"),
	REMOVE_GROUP("[removegroup]"),
	ADD_PERMISSION("[addpermission]"),
	REMOVE_PERMISSION("[removepermission]"),
	SET_PREFIX("[setprefix]"), 
	SET_SUFFIX("[setsuffix]"),
	EFFECT("[effect]"),
	SOUND("[sound]");
	
	private String identifier;
	
	RankupActionType(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public static RankupActionType fromIdentifier(String identifier){
		for(RankupActionType type : values()){
			if (type.getIdentifier().equalsIgnoreCase(identifier)) {
				return type;
			}
		}
		return null;
	}

}
