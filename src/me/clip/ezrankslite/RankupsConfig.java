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
package me.clip.ezrankslite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;

import me.clip.ezrankslite.configuration.ConfigWrapper;
import me.clip.ezrankslite.rankdata.LastRank;
import me.clip.ezrankslite.rankdata.Rankup;
import me.clip.ezrankslite.rankupactions.RankupAction;
import me.clip.ezrankslite.rankupactions.RankupActionType;

public class RankupsConfig {
	
	private EZRanksLite plugin;
	
	private ConfigWrapper rankupsConfig;
	
	private final static Pattern RANKUP_ACTION_MATCHER = Pattern.compile("([\\[][a-zA-Z]+[\\]])");
	
	protected RankupsConfig(EZRanksLite instance) {
		
		plugin = instance;
		
		rankupsConfig = new ConfigWrapper(instance, null, "rankups.yml");
		
		rankupsConfig.createFile("Loading rankups.yml....", "EZRanksLite rankups.yml file"
				+ "\nCreate your rankups in this file."
				+ "\nIf you need a default template, delete the rankups: section and"
				+ "\nlet it regenerate an example rankup."
				+ "\n"
				+ "\nrankup_actions are a list of "
				+ "\nactions which will be executed"
				+ "\nwhen a player successfully ranks up"
				+ "\n"
				+ "\nYou must include required arguments"
				+ "\n<required>, (optional)"
				+ "\nrankup_action list:"
				+ "\n[consolecommand] <command> - perform a console command"
				+ "\n[playercommand] <command> - make the player perform a command"
				+ "\n[message] <message> - send the player a message"
				+ "\n[broadcast] <message> - send the server a message"
				+ "\n[jsonmessage] <json>- send the player a json message"
				+ "\n[jsonbroadcast] <json> - send the server a json message"
				+ "\n[actionbarmessage] <message> - send the player an actionbar message"
				+ "\n[actionbarbroadcast] <message> - send the server an actionbar message"
				+ "\n[addgroup] <group> (world) - add the player to a permissions group"
				+ "\n[removegroup] <group> (world) - remove the player from a permissions group"
				+ "\n[addpermission] <permission> (world) - add a permission node to the player"
				+ "\n[removepermission] <permission> (world) - remove a permission node from a player"
				+ "\n[setprefix] <prefix> - set the players prefix"
				+ "\n[setsuffix] <suffix> - set the players suffix"
				+ "\n[effect] <effect> - play an effect at the players location"
				+ "\n[sound] <sound> <volume> <pitch> - play a sound at the players location"
				+ "\n"
				+ "\n"
				+ "\nPlaceholders can be used in your rankup actions and messages:"
				+ "\n"
				+ "\n%player% - players name"
				+ "\n%displayname% - players displayname"
				+ "\n%world% - players current world"
				+ "\n%rank% - players current rank"
				+ "\n%rankup% - players next rank (if they have one)"
				+ "\n%cost% - cost to rankup"
				+ "\n%cost_formatted% - formatted cost to rankup"
				+ "\n%balance% - players balance"
				+ "\n%balance_formatted% - players formatted balance"
				+ "\n%difference% - amount still needed"
				+ "\n%difference_formatted% - formatted amount still needede"
				+ "\n%progress% - rounded progress % based on cost - balance"
				+ "\n%progressexact% - exact progress % based on cost - balance"
				+ "\n%progressbar% - custom progressbar based on cost - balance"
				+ "\n%rankprefix% - players current rank prefix defined in this file"
				+ "\n%rankupprefix% - players next rank prefix defined in this file"
				+ "\n%lastrank% - last rank available"
				+ "\n%lastrankprefix% - last rank available prefix"
				+ "\n");

		loadDefaults();
		
		rankupsConfig.saveConfig();
		
		rankupsConfig.reloadConfig();
		
		loadRequirementMessage();
		
		loadLastRank();
	}
	
	public void reloadRankupsConfig() {
		
		Rankup.unloadAll();
		
		rankupsConfig.reloadConfig();
		
		rankupsConfig.saveConfig();
		
		loadRequirementMessage();
		
		loadRankups();
		
		loadLastRank();
	}
	
	private void loadDefaults() {
		
		FileConfiguration c = rankupsConfig.getConfig();
		
		c.addDefault("last_rank.rank", "Z");
		c.addDefault("last_rank.prefix", "&8[&bZ&8]");
		c.addDefault("requirement_message", Arrays.asList(new String[] {
				"&8&m------------", 
				"&cYou need &a$&f%cost% &cto rankup to %rankupprefix%",
				"&8&m------------"
		}));
		
		c.options().copyDefaults(true);
	}
	
	private void createDefaultRankup(FileConfiguration c) {
		
		c.set("rankups.A.order", 1);
		c.set("rankups.A.prefix", "[A]");
		c.set("rankups.A.rankup_to", "B");
		c.set("rankups.A.cost", 1000.0);
		c.set("rankups.A.rankup_actions", Arrays.asList(new String[] {
				"[broadcast] &6&l>&b&l> &6%player% &eranked up to &7[&e&l%rankup%&7]", "[addgroup] %rankup%", 
				"[removegroup] %rank%"
		}));
		
		rankupsConfig.saveConfig();
		
		rankupsConfig.reloadConfig();
	}
	
	public void saveRankup(Rankup r) {
		
		FileConfiguration c = rankupsConfig.getConfig();
		
		String p = "rankups." + r.getRank() + ".";
		
		c.set(p + "order", r.getOrder());
		
		c.set(p + "prefix", r.getPrefix());
		
		c.set(p + "rankup_to", r.getRankup());
		
		c.set(p + "cost", r.getCostString());
		
		List<String> actions = new ArrayList<String>();
		
		if (r.getRankupActions() != null) {
			for (RankupAction a : r.getRankupActions()) {
				actions.add(a.getType().getIdentifier() + " " + a.getExecutable());
			}
		}
		
		c.set(p + "rankup_actions", actions);
		
		rankupsConfig.saveConfig();
		
		rankupsConfig.reloadConfig();
	}
	
	public boolean deleteRankup(String rank) {
		
		FileConfiguration c = rankupsConfig.getConfig();
		
		if (c.contains("rankups."+rank)) {
			c.set("rankups."+rank, null);
		
			rankupsConfig.saveConfig();
		
			rankupsConfig.reloadConfig();
			
			return true;
		}
		return false;
	}
	
	public boolean loadLastRank() {
		
		FileConfiguration c = rankupsConfig.getConfig();
		
		String name = c.getString("last_rank.rank");
		
		String prefix = c.getString("last_rank.prefix");
		
		if (name != null && prefix != null) {
			
			LastRank last = new LastRank(name, prefix);
		
			Rankup.setLastRank(last);
			
			plugin.getLogger().info("Last rank set to rank: " + name);
			return true;
		}
		
		plugin.getLogger().warning("No last rank loaded!");
		return false;
	}
	
	public boolean loadRequirementMessage() {
		
		FileConfiguration c = rankupsConfig.getConfig();
		
		List<String> msg = c.getStringList("requirement_message");
		
		if (msg == null || msg.isEmpty()) {
			plugin.getLogger().warning("Rankup requirement message is invalid!");
			return false;
		}
		
		Rankup.setRequirementMessage(msg);
		
		plugin.getLogger().warning("Rankup requirement message successfully loaded!");
		return true;
	}

	public int loadRankups() {
		
		FileConfiguration c = rankupsConfig.getConfig();
		
		if (!c.isConfigurationSection("rankups")) {
			
			plugin.getLogger().warning("No rankups section was found!");
			
			createDefaultRankup(c);
			
			plugin.getLogger().info("Example rankup created!");
			
			return 0;
		}
		
		Set<String> ranks = c.getConfigurationSection("rankups").getKeys(false);
		
		for (String rank : ranks) {
			
			int order = c.getInt("rankups."+rank+".order");
			
			String prefix = c.getString("rankups."+rank+".prefix");
			
			String rankup = c.getString("rankups."+rank+".rankup_to");
			
			String cost = c.getString("rankups."+rank+".cost");
			
			List<String> raw = c.getStringList("rankups."+rank+".rankup_actions");
			
			List<RankupAction> actions = new ArrayList<RankupAction>();
			
			for (String a : raw) {
				
				if (!a.contains(" ")) {
					plugin.getLogger().warning("Rankup action for " + rank + ": " + a + " is improperly defined!");
					plugin.getLogger().info("Correct format: [rankupaction] <executable>");
					continue;
				}
				
				String toMatch = a.split(" ")[0];
				
				if (toMatch.isEmpty()) {
					plugin.getLogger().warning("Rankup action for " + rank + ": " + a + " is improperly defined!");
					plugin.getLogger().info("Correct format: [rankupaction] <executable>");
					continue;
				}
				
				Matcher m = RANKUP_ACTION_MATCHER.matcher(toMatch);
				
				if (m.find()) {
					
					String identifier = m.group(1);
					
					RankupActionType type = RankupActionType.fromIdentifier(identifier);
					
					if (type == null) {
						plugin.getLogger().warning("Rankup action for " + rank + ": " + a + " is not a valid action type!");
						continue;
					}
					
					String executable = a.replace(identifier+" ", "");
					
					if (executable.isEmpty()) {
						plugin.getLogger().warning("Rankup action for " + rank + ": " + a + " does not have an executable String!");
						continue;
					}
					
					RankupAction action = new RankupAction(type, executable);
					
					actions.add(action);
					
				} else {
					plugin.getLogger().warning("Rankup action for " + rank + ": " + a + " did not contain a rankup action!");
					plugin.getLogger().info("Correct format: [rankupaction] <executable>");
					continue;
				}
			}
			
			Rankup r = new Rankup(order, rank, rankup, cost, actions, prefix);
			
			boolean added = r.addRankup();
			
			if (added) {
				
				plugin.getLogger().info("Rankup: " + rank + " to " + rankup + " successfully loaded");
			} else {
				
				plugin.getLogger().warning("Rankup: " + rank + " was not loaded");
			}
		}
		
		int loaded = Rankup.getLoadedRankupAmount();
		
		String amt = loaded > 0 ? String.valueOf(loaded) : "No";
		
		plugin.getLogger().info(amt + " rankup(s) loaded");
		
		if (loaded == 0) {
			plugin.getLogger().info("Create your rankups in the rankups.yml to begin using EZRanksLite!");
		}
		
		return loaded;
	}	
}
