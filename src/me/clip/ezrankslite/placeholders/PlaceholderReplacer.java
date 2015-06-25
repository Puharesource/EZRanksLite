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
package me.clip.ezrankslite.placeholders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.rankdata.Rankup;
import me.clip.ezrankslite.util.EcoUtil;

public class PlaceholderReplacer {
	
	private EZRanksLite plugin;
	
	public PlaceholderReplacer(EZRanksLite instance) {
		plugin = instance;
	}
	
	private final static Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([a-zA-Z0-9_.-]+)[%]");	

	/**
	 * set any placeholders the String provided contains to the actual placeholder values
	 * @param p Player to set the placeholders for
	 * @param r Rankup object related to the player
	 * @param s String to apply placeholders to
	 * @return String provided with all placeholders parsed to the correct values
	 */
	public String setPlaceholders(Player p, Rankup r, String s) {
		
		Matcher m = PLACEHOLDER_PATTERN.matcher(s);
		
		while (m.find()) {
			
			String placeholder = m.group(1);
			
			String replacement = "";
			
			double cost = 0.0;
			
			switch (placeholder) {

			case "player":
				replacement = p.getName();
				break;
			case "displayname":
				replacement = p.getDisplayName();
				break;
			case "world":
				replacement = p.getWorld().getName();
				break;
			case "rank":
			case "rankfrom":
			case "currentrank":
				replacement = r != null && r.getRank() != null ? r.getRank()
						: Rankup.isLastRank(p) && Rankup.getLastRank() != null
								&& Rankup.getLastRank().getRank() != null ? Rankup
								.getLastRank().getRank() : plugin.getPerms()
								.getPrimaryGroup(p) != null ? plugin.getPerms()
								.getPrimaryGroup(p) : "unknown";
				break;
			case "nextrank":
			case "rankto":
			case "rankup":
				replacement = r != null && r.getRankup() != null ? r
						.getRankup() : "none";
				break;
			case "cost":

				

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				long send = (long) cost;

				replacement = String.valueOf(send);
				break;
			case "cost_formatted":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				replacement = EcoUtil.fixMoney(cost);
				break;
			case "difference":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				replacement = String.valueOf(EcoUtil.getDifference(plugin.getEconomy().getBalance(p), cost));
				break;
			case "difference_formatted":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				replacement = EcoUtil.fixMoney(EcoUtil.getDifference(plugin.getEconomy().getBalance(p), cost));
				break;
			case "progress":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				replacement = String.valueOf(EcoUtil.getProgress(plugin.getEconomy().getBalance(p), cost));
				break;
			case "progressbar":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				replacement = EcoUtil.getProgressBar(EcoUtil.getProgressInt(plugin.getEconomy().getBalance(p), cost));
				break;
			case "progressexact":

				if (r != null) {

					cost = r.getCost();

					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}

				replacement = EcoUtil.getProgressExact(plugin.getEconomy().getBalance(p), cost);
				break;
			case "balance":
				replacement = String.valueOf(plugin.getEconomy().getBalance(p));
				break;
			case "balance_formatted":
				replacement = EcoUtil.fixMoney(plugin.getEconomy().getBalance(p));
				break;
			case "rankprefix":
			case "rank_prefix":
				replacement = r != null && r.getPrefix() != null ? r
						.getPrefix() : Rankup.isLastRank(p)
						&& Rankup.getLastRank() != null
						&& Rankup.getLastRank().getPrefix() != null ? Rankup
						.getLastRank().getPrefix() : "";
				break;
			case "lastrank":
			case "last_rank":
				replacement = Rankup.getLastRank() != null
						&& Rankup.getLastRank().getRank() != null ? Rankup
						.getLastRank().getRank() : "";
				break;
			case "lastrank_prefix":
			case "lastrankprefix":
				replacement = Rankup.getLastRank() != null
						&& Rankup.getLastRank().getPrefix() != null ? Rankup
						.getLastRank().getPrefix() : "";
				break;
			case "rankupprefix":
			case "rankup_prefix":
				if (r == null) {
					replacement = "";
					break;
				}
				if (Rankup.getRankup(r.getRankup()) == null) {
					replacement = Rankup.getLastRank() != null
							&& Rankup.getLastRank().getPrefix() != null ? Rankup
							.getLastRank().getPrefix() : "";
					break;
				}

				replacement = Rankup.getRankup(r.getRankup()).getPrefix() != null ? Rankup
						.getRankup(r.getRankup()).getPrefix() : "";
				break;
			}
			
			s = s.replaceAll("%"+placeholder+"%", Matcher.quoteReplacement(replacement));
		}		
		
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
