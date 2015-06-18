package me.clip.ezrankslite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.clip.ezrankslite.commands.AdminCommands;
import me.clip.ezrankslite.commands.RanksCommand;
import me.clip.ezrankslite.commands.RankupCommand;
import me.clip.ezrankslite.configuration.ConfigWrapper;
import me.clip.ezrankslite.effects.EffectsHandler;
import me.clip.ezrankslite.metricslite.MetricsLite;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.multipliers.MultiplierConfig;
import me.clip.ezrankslite.nms.NMSHandler;
import me.clip.ezrankslite.nms.NMS_1_7_R4;
import me.clip.ezrankslite.nms.NMS_1_8_R1;
import me.clip.ezrankslite.nms.NMS_1_8_R2;
import me.clip.ezrankslite.nms.NMS_1_8_R3;
import me.clip.ezrankslite.placeholders.PlaceholderReplacer;
import me.clip.ezrankslite.rankdata.Rankup;
import me.clip.ezrankslite.rankupactions.RankupAction;
import me.clip.ezrankslite.rankupactions.RankupActionHandler;
import me.clip.ezrankslite.rankupactions.RankupActionType;
import me.clip.ezrankslite.updater.UpdateChecker;
import me.clip.ezrankslite.vault.VaultChat;
import me.clip.ezrankslite.vault.VaultEco;
import me.clip.ezrankslite.vault.VaultPerms;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EZRanksLite extends JavaPlugin {
	
	private static EZRanksLite instance;
	
	private MainConfig mainConfig;
	
	private RankupsConfig rankupsConfig;
	
	private MultiplierConfig multiplierConfig;
	
	private ConfigWrapper messages = new ConfigWrapper(this, "", "messages.yml");
	
	private NMSHandler nms;
	
	private EffectsHandler effects;
	
	private VaultEco eco;
	
	private VaultPerms perms;
	
	private VaultChat chat;
	
	private RankupActionHandler rankupActionHandler;
	
	private PlaceholderReplacer placeholderReplacer;
	
	private UpdateChecker updateChecker;
	
	private boolean debug;

	@Override
	public void onEnable() {
		
		eco = new VaultEco();
		
		perms = new VaultPerms();
		
		chat = new VaultChat();
		
		if (eco.hook() && perms.hook() && chat.hook()) {
			
			instance = this;
			
			if (!setupNMS()) {
				getLogger().warning("Could not setup an NMS class for your server version!");
				getLogger().warning("The actionbar, effect, and JSON rankup actions will not be available");
			} else {
				effects = new EffectsHandler();
			}
		
			mainConfig = new MainConfig(this);
		
			mainConfig.loadDefaultConfig();
			
			mainConfig.loadOptions();
			
			debug = getConfig().getBoolean("debug");
			
			if (debug) {
				getLogger().info("debug mode enabled");
			}
			
			messages.createFile("Loading EZRanksLite messages.yml", "EZRanksLite messages file");
			
			loadMessages();
		
			rankupsConfig = new RankupsConfig(this);
		
			if (!convertOldRankupsYml()) {
			
				rankupsConfig.loadRankups();
			} else {
			
				getLogger().info(Rankup.getLoadedRankupAmount() + " 1.x rankups converted and loaded!");
			}
			
			rankupsConfig.loadLastRank();
			
			multiplierConfig = new MultiplierConfig(this);
			
			multiplierConfig.reload();
			
			multiplierConfig.save();

			getLogger().info(multiplierConfig.loadDiscounts() + " rankup cost discounts loaded!");
			
			getLogger().info(multiplierConfig.loadMultipliers() + " rankup cost multipliers loaded!");
			
			rankupActionHandler = new RankupActionHandler(this);
			
			placeholderReplacer = new PlaceholderReplacer(this);
			
			new AdminCommands(this);
			
			new RankupCommand(this);
			
			if (getConfig().getBoolean("ranks_command_enabled")) {
				new RanksCommand(this);
			}
			
			if (getConfig().getBoolean("check_updates")) {
				
				updateChecker = new UpdateChecker(this);
				
				if (updateChecker.checkForUpdate()) {
					
					getLogger().info("An update for EZRanksLite (EZRanksLite v" + UpdateChecker.getLatestVersion() + ")");
					getLogger().info("is available at http://www.spigotmc.org/resources/ezrankslite.762/");
				
				} else {
					
					getLogger().info("You are running the latest version of EZRanksLite!");
				}
			}
			
			if (!startMetricsLite()) {
				debug(false, "Failed to start MetricsLite");
			}
			
		} else {
			
			getLogger().severe("Could not hook into Vault");
			getLogger().severe("EZRanksLite will now disable!");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		
		CostHandler.unload();
		
		RankupCommand.unload();
		
		Rankup.unloadAll();
		
		instance = null;
	}
	
	public void reloadEverything() {
		
		reloadConfig();
		
		saveConfig();
		
		mainConfig.loadOptions();
		
		debug = getConfig().getBoolean("debug");
		
		rankupsConfig.reloadRankupsConfig();

		multiplierConfig.reload();
		
		multiplierConfig.save();

		multiplierConfig.loadDiscounts();
		
		multiplierConfig.loadMultipliers();
		
		RankupCommand.setConfirmToRankup(getConfig().getBoolean("confirm_to_rankup.enabled"));
		
		debug(false, "EZRanksLite has been reloaded");
	}
	
	public void debug(boolean severe, String msg) {
		if (severe) {
			getLogger().severe(msg);
		} else {
			if (debug) {
				System.out.println("[EZRanksLite debug] "+msg);
			}
		}
	}
	
	private boolean setupNMS() {
		
		String version;
		
		try {
			
			version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
			
        } catch (ArrayIndexOutOfBoundsException ex) {
        	
        	return false;
        }
		
		if (version.equals("v1_7_R4")) {
			
			nms = new NMS_1_7_R4();
			
		} else if (version.equals("v1_8_R1")) {
			
			nms = new NMS_1_8_R1();
			
		} else if (version.equals("v1_8_R2")) {
			
			nms = new NMS_1_8_R2();
			
		} else if (version.equals("v1_8_R3")) {
			
			nms = new NMS_1_8_R3();
		}
		
		return nms != null;
	}
	
	private boolean startMetricsLite() {
		try {
			MetricsLite ml = new MetricsLite(this);
			ml.start();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	private void loadMessages() {
		
		Lang.setFile(messages.getConfig());

		for (final Lang value : Lang.values()) {
			messages.getConfig().addDefault(value.getPath(), value.getDefault());
		}

		messages.getConfig().options().copyDefaults(true);
		
		messages.saveConfig();
	}
	
	public static EZRanksLite get() {
		return instance;
	}
	
	public NMSHandler getNMS() {
		return nms;
	}
	
	public MainConfig getMainConfig() {
		return mainConfig;
	}
	
	public RankupsConfig getRankupsConfig() {
		return rankupsConfig;
	}
	
	public EffectsHandler getEffects() {
		return effects;
	}
	
	public RankupActionHandler getActionHandler() {
		return rankupActionHandler;
	}
	
	public PlaceholderReplacer getPlaceholderReplacer() {
		return placeholderReplacer;
	}
	
	public VaultEco getEconomy() {
		return eco;
	}
	
	public VaultPerms getPerms() {
		return perms;
	}
	
	public VaultChat getChat() {
		return chat;
	}
	
	private boolean convertOldRankupsYml() {
		
		File old = new File(getDataFolder(), "old_rankups.yml");
		
		if (!old.exists()) {
			return false;
		}
		
		getLogger().info("EZRanksLite 1.x rankups.yml was found!");
		
		getLogger().info("Converting to 2.0 rankups.yml.......");
		
		FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(old);
		
		Set<String> rankNames = oldConfig.getKeys(false);
		
		if (rankNames == null || rankNames.isEmpty()) {
			getLogger().info("No 1.x rankups to convert!");
			return false;
		}
		
		long start = System.nanoTime();
		
		boolean loadedAtLeastOne = false;
		
		int converted = 0;
		
		for (String rank : rankNames) {
			
			String prefix = oldConfig.getString(rank + ".options.rank_prefix");
			
			int order = oldConfig.getInt(rank + ".options.ranks_display_order");
			
			Set<String> rankups = oldConfig.getConfigurationSection(rank).getKeys(false);
			
			if (rankups == null || rankups.isEmpty()) {
				getLogger().info("No rankups found for rank: " + rank);
				continue;
			}
			
			String rankup = null;
			
			String cost = "0";
			
			List<String> rankupCommands = null;
			
			for (String ru : rankups) {
				
				if (ru.equals("options")) {
					continue;
				}
				
				getLogger().info("Converting rankup: " + rank + " to " + ru + "....");
				
				rankup = ru;
				
				cost = oldConfig.getString(rank + "." + ru + ".cost");
				
				rankupCommands = oldConfig.getStringList(rank + "." + ru + ".rankup_commands");
				
				break;
			}
			
			boolean v = true;
			
			if (rankup == null) {
				getLogger().info("1.x rankup rank for rankup: " + rank + " is invalid!");
				v = false;
			}
			
			if (prefix == null) {
				getLogger().info("1.x prefix for rankup: " + rank + " is invalid!");
				v = false;
			}
			
			if (order < 1) {
				getLogger().info("1.x order for rank: " + rank + " is invalid!");
				v = false;
			}
			
			if (cost == null) {
				getLogger().info("1.x cost for rank: " + rank + " is invalid!");
				v = false;
			}
			
			if (rankupCommands == null || rankupCommands.isEmpty()) {
				getLogger().info("1.x rankup commands for rank: " + rank + " are invalid!");
				v = false;
			}
			
			if (!v) {
				getLogger().info("1.x conversion failed for rank: " + rank + "!");
				continue;
			}
			
			List<RankupAction> actions = new ArrayList<RankupAction>();
			
			for (String command : rankupCommands) {
				
				if (command.startsWith("ezbroadcast ")) {
					
					command = command.replace("ezbroadcast ", "");
					
					actions.add(new RankupAction(RankupActionType.BROADCAST, command));
					
				} else if (command.startsWith("ezmsg ")) {
					
					command = command.replace("ezmsg ", "");
					
					actions.add(new RankupAction(RankupActionType.MESSAGE, command));
					
				} else if (command.startsWith("ezeffect ")) {
					
					command = command.replace("ezeffect ", "");
					
					actions.add(new RankupAction(RankupActionType.EFFECT, command));
					
				} else {
					
					actions.add(new RankupAction(RankupActionType.CONSOLE_COMMAND, command));	
				}
			}
			
			
			Rankup ru = new Rankup(order, rank, rankup, cost, actions, prefix);
			
			rankupsConfig.saveRankup(ru);
			
			getLogger().info("1.x rankup for rank: " + rank + " to " + rankup + " was successful!");
			
			ru.addRankup();
			
			loadedAtLeastOne = true;
			
			converted++;
		}
		
		long finish = System.nanoTime();
		
		long time = finish - start;
		
		time = time / 1000000;
		
		getLogger().info("1.x to 2.x rankups.yml conversion complete!");
		getLogger().info("Total 1.x rankups converted: " + converted);
		getLogger().info("Took " + time + "ms to complete conversion!");
		
		if (loadedAtLeastOne) {
			debug(false, "Removing old_rankups.yml file");
			old.setWritable(true);
			old.delete();
		}
		
		return loadedAtLeastOne;
	}
}
