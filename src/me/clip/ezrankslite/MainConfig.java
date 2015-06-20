package me.clip.ezrankslite;

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
	
	private static List<String> ranksHeader;
	
	private static List<String> ranksFooter;
	
	private static boolean rankupCooldownEnabled;
	
	private static int rankupCooldownTime;
	
	private static boolean checkPrimaryGroup;
	
	public void loadDefaultConfig() {
		
		FileConfiguration c = plugin.getConfig();
		
		c.options().header("EZRanksLite " + plugin.getDescription().getVersion() + " main configuration file"
				+ "\nCreated by extended_clip - http://www.spigotmc.org/resources/authors/extended_clip.1001/");
		
		// config'
		c.addDefault("check_updates", true);
		
		c.addDefault("debug", false);
		c.addDefault("check_primary_group_for_available_rankup", false);
		c.addDefault("confirm_to_rankup.enabled", true);
		c.addDefault("confirm_to_rankup.time", 10);
		c.addDefault("rankup_cooldown.enabled", true);
		c.addDefault("rankup_cooldown.time", 30);
		c.addDefault("ranks_command_enabled", true);
		c.addDefault("ranks.header",
				Arrays.asList(new String[] { "&8&m----------" }));
		c.addDefault("ranks.format_previous_ranks", "&8%rank%: Completed");
		c.addDefault("ranks.format_current_rank", "&7%rank%: &fCurrent rank");
		c.addDefault("ranks.format_incomplete_ranks", "&f%rank%: &a$&f%cost%");
		c.addDefault("ranks.format_last_rank", "&f%lastrank%: &cLast rank!");
		c.addDefault("ranks.footer",
				Arrays.asList(new String[] { "&8&m----------" }));
		
		c.addDefault("money.thousands_format", "k");
		c.addDefault("money.millions_format", "M");
		c.addDefault("money.billions_format", "B");
		c.addDefault("money.trillions_format", "T");
		c.addDefault("money.quadrillions_format", "Q");
		
		c.options().copyDefaults(true);
		
		plugin.saveConfig();
		
		plugin.reloadConfig();
	}
	
	public void loadOptions() {
		
		checkPrimaryGroup = plugin.getConfig().getBoolean("check_primary_group_for_available_rankup");
		
		thousandsFormat = plugin.getConfig().getString("money.thousands_format");
		millionsFormat = plugin.getConfig().getString("money.millions_format");
		billionsFormat = plugin.getConfig().getString("money.billions_format");
		trillionsFormat = plugin.getConfig().getString("money.trillions_format");
		quadrillionsFormat = plugin.getConfig().getString("money.quadrillions_format");
		
		ranksPrevious = plugin.getConfig().getString("ranks.format_previous_ranks");
		ranksCurrent = plugin.getConfig().getString("ranks.format_current_rank");
		ranksNext = plugin.getConfig().getString("ranks.format_incomplete_ranks");
		ranksLastRank = plugin.getConfig().getString("ranks.format_last_rank");
		ranksHeader = plugin.getConfig().getStringList("ranks.header");
		ranksFooter = plugin.getConfig().getStringList("ranks.footer");
		
		rankupCooldownEnabled = plugin.getConfig().getBoolean("rankup_cooldown.enabled");
		rankupCooldownTime = plugin.getConfig().getInt("rankup_cooldown.time") * 1000;
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
		return ranksPrevious != null ? ranksPrevious : "&8%rank%: Completed";
	}
	public static String getRanksCurrentFormat() {
		return ranksCurrent != null ? ranksCurrent : "&7%rank%: &fCurrent rank";
	}
	public static String getRanksNextFormat() {
		return ranksNext != null ? ranksNext : "&f%rank%: &a$&f%cost%";
	}
	public static String getRanksLastFormat() {
		return ranksLastRank != null ? ranksLastRank : "&f%lastrank%: &cLast rank!";
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
}
