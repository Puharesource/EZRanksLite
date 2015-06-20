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

	public String setPlaceholders(Player p, Rankup r, String s) {
		
		Matcher m = PLACEHOLDER_PATTERN.matcher(s);
		
		while (m.find()) {
			
			String placeholder = m.group(1);
			
			String replacement = "";
			
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
				replacement = r != null && r.getRank() != null ? r.getRank() : Rankup.isLastRank(p) && Rankup.getLastRank() != null && Rankup.getLastRank().getRank() != null ? Rankup.getLastRank().getRank() : plugin.getPerms().getPrimaryGroup(p) != null ? plugin.getPerms().getPrimaryGroup(p) : "unknown"; 
				break;
			case "nextrank":
			case "rankto":
			case "rankup":
				replacement = r != null && r.getRankup() != null ? r.getRankup() : "none";
				break;
			case "cost":
				
				double cost = 0.0;
				
				if (r != null) {
					
					cost = r.getCost();
					
					cost = CostHandler.getMultiplier(p, cost);

					cost = CostHandler.getDiscount(p, cost);
				}
				
				long send = (long) cost;
				
				replacement = String.valueOf(send);
				break;
			case "cost_formatted":
				
				double c = 0.0;
				
				if (r != null) {
					
					c = r.getCost();
					
					c = CostHandler.getMultiplier(p, c);

					c = CostHandler.getDiscount(p, c);
				}
				
				replacement = EcoUtil.fixMoney(c);
				break;
			case "difference":
				
				double diff = 0.0;
				
				if (r != null) {
					
					diff = r.getCost();
					
					diff = CostHandler.getMultiplier(p, diff);

					diff = CostHandler.getDiscount(p, diff);
				}
				
				replacement = String.valueOf(EcoUtil.getDifference(plugin.getEconomy().getBalance(p), diff));
				break;
			case "difference_formatted":
				
				double difff = 0.0;
				
				if (r != null) {
					
					difff = r.getCost();
					
					difff = CostHandler.getMultiplier(p, difff);

					difff = CostHandler.getDiscount(p, difff);
				}
				
				replacement = EcoUtil.fixMoney(EcoUtil.getDifference(plugin.getEconomy().getBalance(p), difff));
				break;
			case "progress":
				
				double pro = 0.0;
				
				if (r != null) {
					
					pro = r.getCost();
					
					pro = CostHandler.getMultiplier(p, pro);

					pro = CostHandler.getDiscount(p, pro);
				}
				
				replacement = EcoUtil.getProgress(plugin.getEconomy().getBalance(p), pro);
				break;
			case "progressexact":
				
				double prog = 0.0;
				
				if (r != null) {
					
					prog = r.getCost();
					
					prog = CostHandler.getMultiplier(p, prog);

					prog = CostHandler.getDiscount(p, prog);
				}
				
				replacement = EcoUtil.getProgressExact(plugin.getEconomy().getBalance(p), prog);
				break;
			case "balance":
				replacement = String.valueOf(plugin.getEconomy().getBalance(p));
				break;
			case "balance_formatted":
				replacement = EcoUtil.fixMoney(plugin.getEconomy().getBalance(p));
				break;
			case "rankprefix":
			case "rank_prefix":
				replacement = r != null && r.getPrefix() != null ? r.getPrefix() : Rankup.isLastRank(p) && Rankup.getLastRank() != null && Rankup.getLastRank().getPrefix() != null ? Rankup.getLastRank().getPrefix() : ""; 
				break;
			case "lastrank":
			case "last_rank":
				replacement = Rankup.getLastRank() != null && Rankup.getLastRank().getRank() != null ? Rankup.getLastRank().getRank() : "";
				break;
			case "lastrank_prefix":
			case "lastrankprefix":
				replacement = Rankup.getLastRank() != null && Rankup.getLastRank().getPrefix() != null ? Rankup.getLastRank().getPrefix() : "";
				break;
			case "rankupprefix":
			case "rankup_prefix":
				if (r == null) {
					replacement = "";
					break;
				}
				if (Rankup.getRankup(r.getRankup()) == null) {
					replacement = Rankup.getLastRank() != null && Rankup.getLastRank().getPrefix() != null ? Rankup.getLastRank().getPrefix() : "";
					break;
				}
				
				replacement = Rankup.getRankup(r.getRankup()).getPrefix() != null ? Rankup.getRankup(r.getRankup()).getPrefix() : "";
				break;
			}
			
			s = s.replaceAll("%"+placeholder+"%", Matcher.quoteReplacement(replacement));
		}		
		
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
