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
package me.clip.ezrankslite.commands;

import java.util.Map.Entry;
import java.util.TreeMap;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.Lang;
import me.clip.ezrankslite.MainConfig;
import me.clip.ezrankslite.rankdata.Rankup;
import me.clip.ezrankslite.util.ChatUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RanksCommand implements CommandExecutor {

	private EZRanksLite plugin;
	
	public RanksCommand(EZRanksLite instance) {
		plugin = instance;
		plugin.getCommand("ranks").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		
		if (!(sender instanceof Player)) {
			ChatUtil.msg(sender, "&cuse ezadmin list to view the rank list");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("ezranks.listranks")) {
			//no perms
			ChatUtil.msg(p, Lang.NO_PERMISSION.getConfigValue(new String[] {
					"ezranks.listranks"
			}));
			return true;
		}
		
		if (MainConfig.getRanksHeader() != null && !MainConfig.getRanksHeader().isEmpty()) {
			for (String header : MainConfig.getRanksHeader()) {

				ChatUtil.msg(p, header);
			}
		}
		
		int current = 0;
		
		if (Rankup.getRankup(p) == null) {
			if (Rankup.isLastRank(p)) {
				current = Integer.MAX_VALUE;
			}
		} else {
			current = Rankup.getRankup(p).getOrder();
		}

		TreeMap<Integer, Rankup> rankups = Rankup.getAllRankups();
		
		if (rankups == null || rankups.isEmpty()) {
			
			String msg = Lang.RANKS_NO_RANKS_LOADED.getConfigValue(null);
			
			msg = plugin.getPlaceholderReplacer().setPlaceholders(p, null, msg);
			
			String[] parts = ChatUtil.splitLines(msg);
			
			for (String line : parts) {
				ChatUtil.msg(p, line);
			}
			
			if (MainConfig.getRanksFooter() != null && !MainConfig.getRanksFooter().isEmpty()) {
				for (String footer : MainConfig.getRanksFooter()) {

					ChatUtil.msg(p, footer);
				}
			}
			
			return true;
		}
		
		for (Entry<Integer, Rankup> entries : rankups.entrySet()) {
			
			int place = entries.getKey();
			
			Rankup r = entries.getValue();
			if (place < current) {
				String c = plugin.getPlaceholderReplacer().setPlaceholders(p, r, MainConfig.getRanksPreviousFormat());
				ChatUtil.msg(p, c);
			} else if (place == current) {
				String c = plugin.getPlaceholderReplacer().setPlaceholders(p, r, MainConfig.getRanksCurrentFormat());
				ChatUtil.msg(p, c);
			} else if (place > current) {
				String c = plugin.getPlaceholderReplacer().setPlaceholders(p, r, MainConfig.getRanksNextFormat());
				ChatUtil.msg(p, c);
			}
		}
		
		if (current == Integer.MAX_VALUE && Rankup.isLastRank(p)) {
			String c = plugin.getPlaceholderReplacer().setPlaceholders(p, null, MainConfig.getRanksCurrentFormat());
			ChatUtil.msg(p, c);
		} else {
			String c = plugin.getPlaceholderReplacer().setPlaceholders(p, null, MainConfig.getRanksLastFormat());
			ChatUtil.msg(p, c);
		}
		
		if (MainConfig.getRanksFooter() != null && !MainConfig.getRanksFooter().isEmpty()) {
			for (String footer : MainConfig.getRanksFooter()) {

				ChatUtil.msg(p, footer);
			}
		}
		
		return true;
	}
	
}
