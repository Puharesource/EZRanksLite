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
package me.clip.ezrankslite.rankupactions;

import java.util.Arrays;
import java.util.List;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.rankdata.Rankup;
import me.clip.ezrankslite.util.ChatUtil;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RankupActionHandler {
	
	private EZRanksLite plugin;
	
	public RankupActionHandler(EZRanksLite instance) {
		plugin = instance;
	}

	/**
	 * execute all rankup actions associated with a specific rankup for a specific player
	 * @param p Player to execute the rankup actions for
	 * @param r Rankup associated with the player. This Rankup should be the rankup players have access to at their current rank
	 */
	public void executeRankupActions(Player p, Rankup r) {
		
		List<RankupAction> actions = r.getRankupActions();
		
		for (RankupAction action : actions) {
		
			String executable = plugin.getPlaceholderReplacer().setPlaceholders(p, r, action.getExecutable());
			
			switch (action.getType()) {
			
			case CONSOLE_COMMAND:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executable);
				break;
			case PLAYER_COMMAND:
				p.performCommand(executable);
				break;
			case ADD_GROUP:
				if (executable.contains(" ")) {
					String[] parts = executable.split(" ");
					
					if (parts.length == 2) {
						plugin.getPerms().addGroup(p, parts[0], parts[1]);
					} else {
						plugin.getPerms().addGroup(p, executable, null);
					}
				} else {
					plugin.getPerms().addGroup(p, executable, null);
				}
				break;
			case REMOVE_GROUP:
				if (executable.contains(" ")) {
					String[] parts = executable.split(" ");
					
					if (parts.length == 2) {
						plugin.getPerms().removeGroup(p, parts[0], parts[1]);
					} else {
						plugin.getPerms().removeGroup(p, executable, null);
					}
				} else {
					plugin.getPerms().removeGroup(p, executable, null);
				}
				break;
			case ADD_PERMISSION:
				if (executable.contains(" ")) {
					String[] parts = executable.split(" ");
					
					if (parts.length == 2) {
						plugin.getPerms().addPermission(p, parts[0], parts[1]);
					} else {
						plugin.getPerms().addPermission(p, executable, null);
					}
				} else {
					plugin.getPerms().addPermission(p, executable, null);
				}
				break;
			case REMOVE_PERMISSION:
				if (executable.contains(" ")) {
					String[] parts = executable.split(" ");
					
					if (parts.length == 2) {
						plugin.getPerms().removePermission(p, parts[0], parts[1]);
					} else {
						plugin.getPerms().removePermission(p, executable, null);
					}
				} else {
					plugin.getPerms().removePermission(p, executable, null);
				}
				break;
			case SET_PREFIX:
				plugin.getChat().setPrefix(p, executable);
				break;
			case SET_SUFFIX:
				plugin.getChat().setSuffix(p, executable);
				break;
			case MESSAGE:
				String[] message = ChatUtil.splitLines(executable);
				ChatUtil.msg(p, message);
				break;
			case BROADCAST:
				String[] bCast = ChatUtil.splitLines(executable);
				ChatUtil.broadcast(bCast);
				break;
			case ACTIONBAR_MESSAGE:
				if (plugin.getNMS() != null) {
					plugin.getNMS().sendActionbar(p, executable);
				}
				break;
			case ACTIONBAR_BROADCAST:
				if (plugin.getNMS() != null) {
					plugin.getNMS().broadcastActionbar(executable);
				}
				break;
			case JSON_MESSAGE:
				if (plugin.getNMS() != null) {
					if (executable.contains("&&")) {
						plugin.getNMS().sendJSON(p, Arrays.asList(executable.split("&&")));
					} else {
						plugin.getNMS().sendJSON(p, Arrays.asList(new String[] {
								executable
						}));
					}
				}
				break;
			case JSON_BROADCAST:
				if (plugin.getNMS() != null) {
					if (executable.contains("&&")) {
						plugin.getNMS().broadcastJSON(Arrays.asList(executable.split("&&")));
					} else {
						plugin.getNMS().broadcastJSON(Arrays.asList(new String[] {
								executable
						}));
					}
				}
				break;
			case EFFECT:
				if (plugin.getEffects() != null) {
					plugin.getEffects().playEffect(p, executable);
				}
				break;
			case SOUND:
				
				if (executable.contains(" ")) {
					
					String[] parts = executable.split(" ");
					
					if (parts.length == 3) {
						
						String s = parts[0].toUpperCase();
						
						try {
							
							Sound sound = Sound.valueOf(s);
							
							int volume = Integer.parseInt(parts[1]);
							
							int pitch = Integer.parseInt(parts[2]);
							
							p.playSound(p.getLocation(), sound, volume, pitch);
							
						} catch(Exception e) {
							plugin.getLogger().warning("Sound string " + executable + " is invalid!");
							plugin.getLogger().info("Valid format: <sound> <volume <pitch>");
							plugin.getLogger().info("Valid sound names can be found at the following link:");
							plugin.getLogger().info("https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html");
						}
					}
				}
				break;
			}		
		}
	}
	
}
