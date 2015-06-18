package me.clip.ezrankslite.rankdata;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.rankupactions.RankupAction;

import org.bukkit.entity.Player;

public class Rankup {

	private static TreeMap<Integer, Rankup> rankups = new TreeMap<Integer, Rankup>();
	
	private static LastRank lastRank;
	
	private static List<String> requirementMessage;
	
	private int order;
	
	private String rank;
	
	private String rankup;
	
	private String cost;

	private List<RankupAction> rankupActions;
	
	private String prefix;
	
	public Rankup(int order, @Nonnull String rank, String rankup, String rankupCost, List<RankupAction> rankupActions, String prefix) {
		
		this.order = order;
		this.rank = rank;
		this.rankup = rankup;
		this.cost = rankupCost;
		this.rankupActions = rankupActions;
		this.prefix = prefix;
	}
	
	public Rankup(int order, @Nonnull String rank) {
		this.order = order;
		this.rank = rank;
	}
	
	public Set<RankupCheckResponse> validate() {
		
		Set<RankupCheckResponse> response = EnumSet.noneOf(RankupCheckResponse.class);
		
		if (rankups != null && !rankups.isEmpty()) {
			
			for (Rankup r : rankups.values()) {
				
				if (r.getRank().equals(rank)) {
					response.add(RankupCheckResponse.DUPLICATE_RANK_NAME);
				}
			}
			
			if (rankups.keySet().contains(this.order)) {
				response.add(RankupCheckResponse.DUPLICATE_ORDER);
			} 
		}
		
		if (this.rankup == null) {
			response.add(RankupCheckResponse.NO_RANKUP);
		} 
		
		if (this.order <= 0) {
			response.add(RankupCheckResponse.NEGATIVE_ORDER);
		} 
		
		try {
			
			double c = Double.parseDouble(this.cost);
			
			if (c < 0) {
				response.add(RankupCheckResponse.NEGATIVE_COST);
			}
			
		} catch (Exception e) {
			response.add(RankupCheckResponse.INVALID_COST);
		}
		
		if (this.rankupActions == null || this.rankupActions.isEmpty()) {
			response.add(RankupCheckResponse.NO_RANKUP_ACTIONS);
		} 
		
		if (this.prefix == null) {
			response.add(RankupCheckResponse.NO_RANK_PREFIX);
		}
		
		if (response.isEmpty()) {
			response.add(RankupCheckResponse.VALID);
		}
		
		return response;
	}
	
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getRankup() {
		return rankup;
	}
	
	public void setRankup(String rankup) {
		this.rankup = rankup;
	}
	
	public double getCost() {
		return Double.parseDouble(cost);
	}
	
	public String getCostString() {
		return cost;
	}
	
	public void setCost(String cost) {
		this.cost = cost;
	}
	
	public List<RankupAction> getRankupActions() {
		return rankupActions;
	}
	
	public void setRankupActions(List<RankupAction> rankupActions) {
		this.rankupActions = rankupActions;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public boolean addRankup() {

		Set<RankupCheckResponse> validate = validate();
		
		if (validate.contains(RankupCheckResponse.VALID)) {
			
			if (rankups == null) {
				rankups = new TreeMap<Integer, Rankup>();
			}
			
			rankups.put(this.order, this);
			
			return true;
		}
		
		EZRanksLite.get().getLogger().warning("The rankup " + rank + " is invalid:");
		
		Iterator<RankupCheckResponse> it = validate.iterator();
		
		while (it.hasNext()) {
			
			EZRanksLite.get().getLogger().warning(it.next().getMessage());
		}
		
		return false;
	}
	
	public boolean removeRankup() {
		if (rankups == null || rankups.isEmpty()) {
			return false;
		}
		if (rankups.containsKey(this.order)) {
			
			return rankups.remove(this.order) != null;
		}
		return false;
	}
	
	
	public static Rankup getRankup(String rank) {
		
		if (rankups == null || rankups.isEmpty()) {
			return null;
		}
		
		Iterator<Rankup> i = rankups.values().iterator();
		while (i.hasNext()) {
			Rankup r = i.next();
			if (r.getRank().equals(rank)) {
				return r;
			}
		}
		return null;
	}
	
	public static Rankup getRankup(Player p) {
		
		if (rankups == null || rankups.isEmpty()) {
			return null;
		}
		
		if (isLastRank(p)) {
			return null;
		}
		
		for (int i = rankups.size();i>=1;i--) {
			
			Rankup r = rankups.get(i);
			
			if (p.hasPermission("ezranks.rank."+r.getRank())) {
				return r;
			}
		}
	
		return null;
	}
	
	public static int getLoadedRankupAmount() {
		return rankups != null ? rankups.size() : 0;
	}
	
	public static void unloadAll() {
		rankups = null;
	}

	public static LastRank getLastRank() {
		return lastRank;
	}

	public static void setLastRank(LastRank lastRank) {
		Rankup.lastRank = lastRank;
	}
	
	public static boolean isLastRank(Player p) {
		return lastRank != null && p.hasPermission("ezranks.lastrank");
	}
	
	public static boolean isLastRank(String rank) {
		return lastRank != null && lastRank.getRank() != null && lastRank.getRank().equalsIgnoreCase(rank);
	}

	public static List<String> getRequirementMessage() {
		return requirementMessage;
	}

	public static void setRequirementMessage(List<String> requirementMessage) {
		Rankup.requirementMessage = requirementMessage;
	}
	
	public static TreeMap<Integer, Rankup> getAllRankups() {
		return rankups;
	}
}
