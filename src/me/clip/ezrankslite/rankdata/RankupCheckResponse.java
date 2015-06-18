package me.clip.ezrankslite.rankdata;


public enum RankupCheckResponse {

	VALID("Valid"), 
	DUPLICATE_RANK_NAME("Already a rank loaded with this name"),
	NO_RANKUP("Does not contain a rankup name"), 
	NEGATIVE_ORDER("Has an incorrect order"),
	DUPLICATE_ORDER("Already a rankup loaded with the same order"), 
	NEGATIVE_COST("Has a negative cost"), 
	INVALID_COST("Has an invalid cost"), 
	NO_RANKUP_ACTIONS("No rankup actions listed"), 
	NO_RANK_PREFIX("No rank prefix specified");
	
	private String message;
	
	RankupCheckResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
