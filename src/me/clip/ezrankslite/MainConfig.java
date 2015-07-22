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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class MainConfig {

	private EZRanksLite plugin;
	
	public MainConfig(EZRanksLite instance) {
		plugin = instance;
	}
	
	private static String thousandsFormat;
	
	private static String millionsFormat;
	
	private static String billionsFormat;
	
	private static String trillionsFormat;
	
	private static String quadrillionsFormat;
	
	private static String ranksPrevious;
	
	private static String ranksCurrent;
	
	private static String ranksNext;
	
	private static String ranksLastRank;
	
	private static String ranksIsLastRank;
	
	private static List<String> ranksHeader;
	
	private static List<String> ranksFooter;
	
	private static boolean rankupCooldownEnabled;
	
	private static int rankupCooldownTime;
	
	private static boolean checkPrimaryGroup;
	
	private static String barHasColor;
	
	private static String barNeedsColor;
	
	private static String barEndColor;
	
	private static String barLeft;
	
	private static String barRight;
	
	private static String barChar;
	
	private static String barFull;
	
	private static boolean useCustomFormat;
	
	private static NumberFormat customFormat;

	
	public void loadDefaultConfig() {
		
		FileConfiguration c = plugin.getConfig();
		
		c.options().header("EZRanksLite " + plugin.getDescription().getVersion() + " main configuration file"
				+ "\nCreated by extended_clip - http://www.spigotmc.org/resources/authors/extended_clip.1001/");
		
		// config'
		c.addDefault("check_updates", true);
		
		c.addDefault("debug", false);
		c.addDefault("check_primary_group_for_available_rankup", false);
		c.addDefault("chat_prefix_enabled", false);
		c.addDefault("log_rankups_to_file", false);
		c.addDefault("confirm_to_rankup.enabled", true);
		c.addDefault("confirm_to_rankup.time", 10);
		
		c.addDefault("rankup_cooldown.enabled", true);
		c.addDefault("rankup_cooldown.time", 30);
		
		c.addDefault("ranks_command_enabled", true);
		c.addDefault("ranks.header", Arrays.asList(new String[] { "&8&m----------" }));
		c.addDefault("ranks.format_previous_ranks", "&8%rank% to %rankto%: Completed");
		c.addDefault("ranks.format_current_rank", "&f%rank% to %rankto%: &a$&f%cost%");
		c.addDefault("ranks.format_incomplete_ranks", "&7%rank% to %rankto%: &a$&f%cost%");
		c.addDefault("ranks.format_last_rank", "&f%lastrank%: &cLast rank!");
		c.addDefault("ranks.format_is_last_rank", "&f%lastrank%: &aYou are the last rank!");
		c.addDefault("ranks.footer", Arrays.asList(new String[] { "&8&m----------" }));
		
		c.addDefault("money.use_custom_format", false);
		c.addDefault("money.custom_format", "#,###.00");
		
		c.addDefault("money.thousands_format", "k");
		c.addDefault("money.thousands_format", "k");
		c.addDefault("money.millions_format", "M");
		c.addDefault("money.billions_format", "B");
		c.addDefault("money.trillions_format", "T");
		c.addDefault("money.quadrillions_format", "Q");
		
		
		c.addDefault("progress_bar.has_color", "&a");
		c.addDefault("progress_bar.needs_color", "&8");
		c.addDefault("progress_bar.end_color", "&e");
		c.addDefault("progress_bar.left_character", "[");
		c.addDefault("progress_bar.bar_character", ":");
		c.addDefault("progress_bar.right_character", "]");
		c.addDefault("progress_bar.is_full", "&a/rankup");
		
		c.options().copyDefaults(true);
		
		plugin.saveConfig();
		
		plugin.reloadConfig();
	}
	
	public void loadOptions() {
		
		checkPrimaryGroup = plugin.getConfig().getBoolean("check_primary_group_for_available_rankup");
		
		useCustomFormat = plugin.getConfig().getBoolean("money.use_custom_format");
		
		if (useCustomFormat) {
			
			try {
				customFormat = new DecimalFormat(plugin.getConfig().getString("money.custom_format"));
			} catch (NullPointerException | IllegalArgumentException ex) {
				customFormat = new DecimalFormat("#,###.00");
				plugin.getLogger().warning("custom money format was invalid! Defaulting to format: #,###.00");
			}
		}
		
		thousandsFormat = plugin.getConfig().getString("money.thousands_format");
		millionsFormat = plugin.getConfig().getString("money.millions_format");
		billionsFormat = plugin.getConfig().getString("money.billions_format");
		trillionsFormat = plugin.getConfig().getString("money.trillions_format");
		quadrillionsFormat = plugin.getConfig().getString("money.quadrillions_format");
		
		ranksPrevious = plugin.getConfig().getString("ranks.format_previous_ranks");
		ranksCurrent = plugin.getConfig().getString("ranks.format_current_rank");
		ranksNext = plugin.getConfig().getString("ranks.format_incomplete_ranks");
		ranksLastRank = plugin.getConfig().getString("ranks.format_last_rank");
		ranksIsLastRank = plugin.getConfig().getString("ranks.format_is_last_rank");
		ranksHeader = plugin.getConfig().getStringList("ranks.header");
		ranksFooter = plugin.getConfig().getStringList("ranks.footer");
		
		rankupCooldownEnabled = plugin.getConfig().getBoolean("rankup_cooldown.enabled");
		rankupCooldownTime = plugin.getConfig().getInt("rankup_cooldown.time") * 1000;
		
		barHasColor = plugin.getConfig().getString("progress_bar.has_color");
		barNeedsColor = plugin.getConfig().getString("progress_bar.needs_color");
		barEndColor = plugin.getConfig().getString("progress_bar.end_color");
		barLeft = plugin.getConfig().getString("progress_bar.left_character");
		barRight = plugin.getConfig().getString("progress_bar.right_character");
		barChar = plugin.getConfig().getString("progress_bar.bar_character");
		barFull = plugin.getConfig().getString("progress_bar.is_full");
	}
	
	public static boolean useCustomFormat() {
		return useCustomFormat && customFormat != null;
	}
	
	public static NumberFormat getCustomFormat() {
		return customFormat;
	}
	
	public static boolean checkPrimaryGroupForRankups() {
		return checkPrimaryGroup;
	}
	
	public static String getThousandsFormat() {
		return thousandsFormat != null ? thousandsFormat : "k";
	}
	
	public static String getMillionsFormat() {
		return millionsFormat != null ? millionsFormat : "M";
	}
	
	public static String getBillionsFormat() {
		return billionsFormat != null ? billionsFormat : "B";
	}
	
	public static String getTrillionsFormat() {
		return trillionsFormat != null ? trillionsFormat : "T";
	}
	
	public static String getQuadrillionsFormat() {
		return quadrillionsFormat != null ? quadrillionsFormat : "Q";
	}
	
	public static String getRanksPreviousFormat() {
		return ranksPrevious != null ? ranksPrevious : "&8%rank% to %rankto%: Completed";
	}
	
	public static String getRanksCurrentFormat() {
		return ranksCurrent != null ? ranksCurrent : "&f%rank% to %rankto%: &a$&f%cost%";
	}
	
	public static String getRanksNextFormat() {
		return ranksNext != null ? ranksNext : "&7%rank% to %rankto%: &a$&f%cost%";
	}
	
	public static String getRanksLastFormat() {
		return ranksLastRank;
	}
	
	public static String getRanksIsLastFormat() {
		return ranksIsLastRank;
	}
	
	public static List<String> getRanksHeader() {
		return ranksHeader;
	}
	
	public static List<String> getRanksFooter() {
		return ranksFooter;
	}
	
	public static boolean rankupCooldownEnabled() {
		return rankupCooldownEnabled;
	}
	
	public static int rankupCooldownTime() {
		return rankupCooldownTime;
	}
	
	public static String getBarHasColor() {
		return barHasColor != null ? barHasColor : "&a";
	}
	
	public static String getBarNeedsColor() {
		return barNeedsColor != null ? barNeedsColor : "&8";
	}
	
	public static String getBarEndColor() {
		return barEndColor != null ? barEndColor : "&e";
	}
	
	public static String getBarLeft() {
		return barLeft != null ? barLeft : "[";
	}
	
	public static String getBarRight() {
		return barRight != null ? barRight : "]";
	}
	
	public static String getBarChar() {
		return barChar != null ? barChar : ":";
	}
	
	public static String getBarIsFull() {
		return barFull != null ? barFull : "&aRankup";
	}
}
