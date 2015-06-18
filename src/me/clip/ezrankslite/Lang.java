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

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Lang {
	/**
	 * No permission
	 */
	NO_PERMISSION("no_permission", "&cYou don't have the permission node &f{0} &cto do that!"),
	/**
	 * Rankup last rank
	 */
	RANKUP_LAST_RANK("rankup_last_rank", "&7You are at the last rank and have no more rankups."),
	/**
	 * Rankup no rankups
	 */
	RANKUP_NO_RANKUPS("rankup_no_rankups", "&cYou don't have any rankups available at your current rank."),
	
	RANKUP_CONFIRM("rankup_confirm_message", "&aAre you sure you want to rankup to &f%rankup% for &a$&f%cost%?\n&aType &7/rankup &ato confirm."),
	
	
	RANKUP_ON_COOLDOWN("rankup_on_cooldown", "&cYou need to wait %time% more seconds until you can rankup again!"),
	
	RANKS_NO_RANKS_LOADED("ranks_no_ranks_loaded", "&cThere are no ranks loaded!")
	;

	private String path, def;
	private static FileConfiguration LANG;

	Lang(final String path, final String start) {
		this.path = path;
		this.def = start;
	}

	public static void setFile(final FileConfiguration config) {
		LANG = config;
	}

	public String getDefault() {
		return this.def;
	}

	public String getPath() {
		return this.path;
	}
	
	public String getConfigValue(final String[] args) {
		String value = ChatColor.translateAlternateColorCodes('&',
				LANG.getString(this.path, this.def));

		if (args == null)
			return value;
		else {
			if (args.length == 0)
				return value;

			for (int i = 0; i < args.length; i++) {
				value = value.replace("{" + i + "}", args[i]);
			}
		}

		return value;
	}
}
