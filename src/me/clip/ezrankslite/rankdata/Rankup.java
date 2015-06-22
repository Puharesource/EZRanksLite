/* This file is a class of EZRanksLite
 * @author Ryan McCarthy
 * 
 * 
 * EZRanksLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 
 * EZRanksLite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.clip.ezrankslite.rankdata;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.MainConfig;
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
	
	/**
	 * check if this rankup is a valid rankup with all fields set to the correct values
	 * @return Set<RankupCheckResponse> of responses, this set will return RankupCheckResponse.VALID if the rankup is valid
	 */
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
	
	/**
	 * get the order of this rankup
	 * @return int order
	 */
	public int getOrder() {
		return order;
	}
	
	/**
	 * set the order of this rankup
	 * @param order int new order
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	
	/**
	 * get the rank players need to be to access this rankup
	 * @return Rank name associated with this rankup
	 */
	public String getRank() {
		return rank;
	}
	
	/**
	 * set the rank players need to be to have access to this rankup
	 * @param rank new rank name associated with this rankup
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	/**
	 * get the name of the rank players with this rankup will go to when they /rankup
	 * @return name of the rank players will be when they /rankup
	 */
	public String getRankup() {
		return rankup;
	}
	
	/**
	 * set the name of the rank players with this rankup will go to when they /rankup
	 * @param rankup name of the rank players will rankup to
	 */
	public void setRankup(String rankup) {
		this.rankup = rankup;
	}
	
	/**
	 * get the raw double amount that this rankup will cost
	 * @return Double amount this rankup cost 
	 */
	public double getCost() {
		return Double.parseDouble(cost);
	}
	
	/**
	 * get the String amount associated with this rankup
	 * @return cost amount as a String
	 */
	public String getCostString() {
		return cost;
	}
	
	/**
	 * set the cost amount this rankup will be when players rankup
	 * @param cost Cost as a String, this String should parse to a double
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}
	
	/**
	 * get a list of RankupActions that should be performed when players rankup from this rankup
	 * @return List<RankupAction> of actions to perform
	 */
	public List<RankupAction> getRankupActions() {
		return rankupActions;
	}
	
	/**
	 * set a list of RankupActions that should be performed when players rankup from this rankup
	 * @param rankupActions List<RankupAction> of actions to perform
	 */
	public void setRankupActions(List<RankupAction> rankupActions) {
		this.rankupActions = rankupActions;
	}
	
	/**
	 * get the prefix String associated with this rankup
	 * @return String prefix associated with this rankup
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * set the prefix String associated with this rankup
	 * @param prefix new String prefix this rankup should be
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * load this rankup in the rankups Map if it is a valid rankup
	 * @return true if the rankup was valid and added, false if the rankup was invalid
	 */
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
	
	/**
	 * remove this rankup from the loaded rankups map
	 * @return true if the rankup was removed, false if it was not loaded
	 */
	public boolean removeRankup() {
		if (rankups == null || rankups.isEmpty()) {
			return false;
		}
		if (rankups.containsKey(this.order)) {
			
			return rankups.remove(this.order) != null;
		}
		return false;
	}
	
	/**
	 * get the rankup associated with a specific rank
	 * @param rank Rank name players need to be to have access to this rankup
	 * @return Rankup object associated with the provided rank name, null if there was no Rankup object loaded with the name provided
	 */
	public static Rankup getRankup(String rank) {
		
		if (rankups == null || rankups.isEmpty()) {
			return null;
		}
		
		Iterator<Rankup> i = rankups.values().iterator();
		
		while (i.hasNext()) {
			Rankup r = i.next();
			if (r.getRank().equalsIgnoreCase(rank)) {
				return r;
			}
		}
		return null;
	}
	
	/**
	 * get the rankup associated with a specific player
	 * @param p Player to get the Rankup object for
	 * @return Rankup object player currently has access to. This Rankup object holds information specific to the players current rank. 
	 * Returns null if player is the last rank or no rankup is available
	 */
	public static Rankup getRankup(Player p) {
		
		if (rankups == null || rankups.isEmpty()) {
			return null;
		}
		
		if (isLastRank(p)) {
			return null;
		}
		
		if (MainConfig.checkPrimaryGroupForRankups()) {
			
			String primary = EZRanksLite.get().getPerms().getPrimaryGroup(p);
			
			if (primary == null) {
				return null;
			}
			
			for (int i = rankups.size();i>=1;i--) {
				
				if (!rankups.containsKey(i)) {
					continue;
				}
				
				Rankup r = rankups.get(i);
				
				if (r == null) {
					continue;
				}
				
				if (r.getRank().equalsIgnoreCase(primary)) {
					return r;
				}
			}	
		} else {
			
			for (int i = rankups.size();i>=1;i--) {
				
				if (!rankups.containsKey(i)) {
					continue;
				}
				
				Rankup r = rankups.get(i);
				
				if (r == null) {
					continue;
				}
				
				if (p.hasPermission("ezranks.rank."+r.getRank())) {
					return r;
				}
			}			
		}
		
		return null;
	}
	
	/**
	 * get the amount of rankups loaded
	 * @return amount of rankup objects loaded into the rankups map
	 */
	public static int getLoadedRankupAmount() {
		return rankups != null ? rankups.size() : 0;
	}
	
	/**
	 * unload all rankups
	 */
	public static void unloadAll() {
		rankups = null;
	}

	/**
	 * get the last rank object that was loaded
	 * @return LastRank object
	 */
	public static LastRank getLastRank() {
		return lastRank;
	}

	/**
	 * set the last rank object
	 * @param lastRank LastRank object that should be loaded
	 */
	public static void setLastRank(LastRank lastRank) {
		Rankup.lastRank = lastRank;
	}
	
	/**
	 * check if a player is the last rank
	 * @param p Player to check
	 * @return true if player is the last rank, false otherwise
	 */
	public static boolean isLastRank(Player p) {
		
		if (lastRank == null || lastRank.getRank() == null) {
			return false;
		}
		
		if (MainConfig.checkPrimaryGroupForRankups()) {
			
			String primary = EZRanksLite.get().getPerms().getPrimaryGroup(p);
			
			if (primary == null) {
				return false;
			}
			
			return primary.equalsIgnoreCase(lastRank.getRank());
			
		} else {
			return p.hasPermission("ezranks.lastrank");
		}
	}
	
	/**
	 * check if a rank name is the last rank
	 * @param rank Rank name to check
	 * @return true if the rank provided is the last rank, false otherwise
	 */
	public static boolean isLastRank(String rank) {
		return lastRank != null && lastRank.getRank() != null && lastRank.getRank().equalsIgnoreCase(rank);
	}

	/**
	 * get the requirement message sent to players if they do not have enough money to rankup
	 * @return List<String> requirement message sent to players if they do not have enough money to rankup
	 */
	public static List<String> getRequirementMessage() {
		return requirementMessage;
	}

	/**
	 * set the requirement message sent to players if they do not have enough money to rankup
	 * @param requirementMessage new requirement message to send
	 */
	public static void setRequirementMessage(List<String> requirementMessage) {
		Rankup.requirementMessage = requirementMessage;
	}
	
	/**
	 * get a copy of the map containing all loaded rankups
	 * @return TreeMap<Integer, Rankup> rankups copy
	 */
	public static TreeMap<Integer, Rankup> getAllRankups() {
		return rankups != null ? new TreeMap<Integer, Rankup>(rankups) : null;
	}
}
