package me.clip.ezrankslite.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.Lang;
import me.clip.ezrankslite.rankdata.Rankup;
import me.clip.ezrankslite.rankupactions.RankupAction;
import me.clip.ezrankslite.rankupactions.RankupActionType;
import me.clip.ezrankslite.util.ChatUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {
	
	private EZRanksLite plugin;
	
	public AdminCommands(EZRanksLite instance) {
		plugin = instance;
		plugin.getCommand("ezrankslite").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

		if (args.length == 0) {
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			ChatUtil.msg(s, "&bEZ&fRanksLite &7version: &b"+plugin.getDescription().getVersion());
			ChatUtil.msg(s, "&fCreated by: &bextended_clip");
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			return true;
		}
		
		if (s instanceof Player) {
			
			Player p = (Player) s;
			
			if (!p.hasPermission("ezranks.admin")) {
				ChatUtil.msg(s, Lang.NO_PERMISSION.getConfigValue(new String[] {
						"ezranks.admin"
				}));
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("help")) {
			
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			ChatUtil.msg(s, "&bEZ&fRanksLite &eAdmin Help");
			ChatUtil.msg(s, "&f");
			ChatUtil.msg(s, "&e/ezadmin createrankup <rank> <rankup> <cost>");
			ChatUtil.msg(s, "&fcreate a new rankup");
			ChatUtil.msg(s, "&e/ezadmin deleterankup <rank>");
			ChatUtil.msg(s, "&fdelete a loaded rankup");
			ChatUtil.msg(s, "&e/ezadmin list");
			ChatUtil.msg(s, "&flist all loaded rankups");
			ChatUtil.msg(s, "&e/ezadmin info <rank>");
			ChatUtil.msg(s, "&fview info for a loaded rankup");
			ChatUtil.msg(s, "&e/ezadmin reload");
			ChatUtil.msg(s, "&freload &bEZ&fRanksLite");
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			return true;
		} else if (args[0].equalsIgnoreCase("reload")) {
			
			plugin.reloadEverything();
			
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			ChatUtil.msg(s, "&bEZ&fRanksLite &asuccessfully reloaded!");
			ChatUtil.msg(s, Rankup.getLoadedRankupAmount() + " &brankups loaded!");
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			
			return true;
		} else if (args[0].equalsIgnoreCase("createrankup")) {
			
			if (args.length != 4) {
				ChatUtil.msg(s, "&cIncorrect usage! &a/ezadmin createrankup <rank> <rankup> <cost>");
				return true;
			}
			
			String rank = args[1];
			
			if (Rankup.getRankup(rank) != null) {
				ChatUtil.msg(s, "&cThere is already a rankup loaded for the rank &f" + rank);
				return true;
			}
			
			String rankup = args[2];
			
			double cost = 0;
			
			try {
				cost = Double.parseDouble(args[3]);
			} catch (NumberFormatException e) {
				ChatUtil.msg(s, "&cInvalid cost! &a/ezadmin createrankup <rank> <rankup> <cost>");
				return true;
			}
			
			if (cost < 0) {
				ChatUtil.msg(s, "&cInvalid cost! &a/ezadmin createrankup <rank> <rankup> <cost>");
				return true;
			}
			
			Rankup r = new Rankup(Rankup.getLoadedRankupAmount() > 0 ? Rankup.getLoadedRankupAmount()+1 : 1, rank);
			
			r.setRankup(rankup);
			
			r.setCost(args[3]);
			
			r.setPrefix("&8[&f" + rank + "&8]");
			
			List<RankupAction> actions = new ArrayList<RankupAction>();
			
			actions.add(new RankupAction(RankupActionType.ADD_GROUP, rankup));
			actions.add(new RankupAction(RankupActionType.REMOVE_GROUP, rank));
			actions.add(new RankupAction(RankupActionType.BROADCAST, "&6&l>&b&l> &6%player% &eranked up to &7[&e&l%rankup%&7]"));
			
			r.setRankupActions(actions);
			
			r.addRankup();
			
			plugin.getRankupsConfig().saveRankup(r);
			
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			ChatUtil.msg(s, "&bRankup &f" + rank + " &bto &f" + rankup + " &bsuccessfully created!");
			ChatUtil.msg(s, "&bRankup cost: &f" + cost);
			ChatUtil.msg(s, "&7Assign this rankup to a group by giving it the permission &fezranks.rank." + rank);
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			
			return true;
		} else if (args[0].equalsIgnoreCase("deleterankup")) { 
			
			if (args.length != 2) {
				ChatUtil.msg(s, "&cIncorrect usage! &a/ezadmin deleterankup <rank>");
				return true;
			}
			
			String rank = args[1];
			
			Rankup r = Rankup.getRankup(rank);
			
			if (r == null) {
				ChatUtil.msg(s, "&cThere is no rankup loaded for the rank &f" + rank);
				return true;
			}
			
			if (r.removeRankup()) {
				
				plugin.getRankupsConfig().deleteRankup(r.getRank());
				
				ChatUtil.msg(s, "&aRankup for rank &f" + rank + " &awas successfully removed!");
				
			} else {
				ChatUtil.msg(s, "&cThe rankup for &f" + rank + " &awas not loaded?");
			}
			
			return true;
			
		} else if (args[0].equalsIgnoreCase("list")) { 
			
			if (Rankup.getAllRankups() == null || Rankup.getAllRankups().isEmpty()) {
				ChatUtil.msg(s, "&cThere are no rankups loaded!");
				return true;
			}
			
			Map<Integer, Rankup> rankups = Rankup.getAllRankups();
			
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			ChatUtil.msg(s, "&aThere are &f" + rankups.size() + " &arankups loaded!");
			for (Rankup r : rankups.values()) {
				ChatUtil.msg(s, "&e" + r.getOrder() + "&7: &f" + r.getRank() + " &7to &f" + r.getRankup());	
			}
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			return true;
			
		} else if (args[0].equalsIgnoreCase("info")) { 
			
			if (args.length != 2) {
				ChatUtil.msg(s, "&cIncorrect usage! &a/ezadmin info <rank>");
				return true;
			}
			
			String rank = args[1];
			
			Rankup r = Rankup.getRankup(rank);
			
			if (r == null) {
				ChatUtil.msg(s, "&cThere is no rankup loaded for the rank &f" + rank);
				return true;
			}
			
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			ChatUtil.msg(s, "&aRankup info for rankup: &f" + rank + " &ato &f" + r.getRankup());
			ChatUtil.msg(s, "&aRankup order: &f" + r.getOrder());
			ChatUtil.msg(s, "&aRankup cost: &f" + r.getCostString());
			ChatUtil.msg(s, "&aRank prefix: &f" + r.getPrefix());
			ChatUtil.msg(s, "&aRankup actions: &f" + (r.getRankupActions() != null ? r.getRankupActions().size() : 0));
			
			if (r.getRankupActions() != null) {
				for (RankupAction action : r.getRankupActions()) {
					ChatUtil.msg(s, action.getType().getIdentifier() + " " + action.getExecutable());	
				}
			}
			ChatUtil.msg(s, "&8&m-----------------------------------------------------");
			return true;
		} else {
			
		
			ChatUtil.msg(s, "&cUnknown command!");
			return true;
		}
		
	}

}
