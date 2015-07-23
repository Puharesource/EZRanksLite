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

import me.clip.ezrankslite.commands.AdminCommands;
import me.clip.ezrankslite.commands.RanksCommand;
import me.clip.ezrankslite.commands.RankupCommand;
import me.clip.ezrankslite.configuration.ConfigWrapper;
import me.clip.ezrankslite.effects.EffectsHandler;
import me.clip.ezrankslite.hooks.FeatherboardTempHook;
import me.clip.ezrankslite.listeners.ChatListener;
import me.clip.ezrankslite.listeners.RankupListener;
import me.clip.ezrankslite.metricslite.MetricsLite;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.multipliers.MultiplierConfig;
import me.clip.ezrankslite.nms.*;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * EZRanksLite main class
 * @author Ryan McCarthy
 *
 */
public class EZRanksLite extends JavaPlugin {
	
	private static EZRanksLite instance;

	private static EZAPI oldApi;

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
		oldApi = new EZAPI();

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
			
			if (getConfig().getBoolean("log_rankups_to_file")) {
				
				new RankupListener(this);
			}
			
			rankupActionHandler = new RankupActionHandler(this);
			
			placeholderReplacer = new PlaceholderReplacer(this);
			
			new AdminCommands(this);
			
			new RankupCommand(this);
			
			if (getConfig().getBoolean("ranks_command_enabled")) {
				new RanksCommand(this);
			}
			
			if (getConfig().getBoolean("chat_prefix_enabled")) {
				new ChatListener(this);
				getLogger().info("Chat listener registered, Add {ezrankslite_rankprefix} to your chat formatting plugin for chat prefix integration");
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
			
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {

				@Override
				public void run() {
					if (Bukkit.getPluginManager().isPluginEnabled("FeatherBoard")) {
						
						getLogger().info("Adding hotfix placeholders to FeatherBoard.....");
						
						new FeatherboardTempHook(EZRanksLite.get()).addPlaceholders();
					}
					
				}
				
			}, 20L);
			
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
	
	/**
	 * reload all settings
	 */
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

	/**
	 * @return EZAPI is a deprecated class, that shouldn't be used.
	 */
	@Deprecated
	public static EZAPI getApi() {
		return oldApi;
	}

	/**
	 * Send a debug message to console if debug mode is enabled
	 * or if the message is severe
	 * @param severe if the message should be forced even if debug is disabled
	 * @param msg message to send
	 */
	public void debug(boolean severe, String msg) {
		if (severe) {
			getLogger().severe(msg);
		} else {
			if (debug) {
				System.out.println("[EZRanksLite debug] "+msg);
			}
		}
	}
	
	/**
	 * sets up the version specific nms class for the actionbar and json packet sending
	 * @return true if nms class was assigned, false otherwise
	 */
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
	
	/**
	 * load the default messages from the Lang enum to the messages.yml
	 */
	private void loadMessages() {
		
		Lang.setFile(messages.getConfig());

		for (final Lang value : Lang.values()) {
			messages.getConfig().addDefault(value.getPath(), value.getDefault());
		}

		messages.getConfig().options().copyDefaults(true);
		
		messages.saveConfig();
	}
	
	/**
	 * get the static instance of EZRanksLite
	 * @return EZRanksLite instance
	 */
	public static EZRanksLite get() {
		return instance;
	}
	
	/**
	 * get the NMSHandler that was assigned on startup
	 * @return null if server version is not compatible, NMSHandler otherwise
	 */
	public NMSHandler getNMS() {
		return nms;
	}
	
	/**
	 * get the main config class for EZRanksLite
	 * @return MainConfig instance loaded
	 */
	public MainConfig getMainConfig() {
		return mainConfig;
	}
	
	/**
	 * get the rankups config class for EZRanksLite
	 * @return RankupsConfig instance loaded
	 */
	public RankupsConfig getRankupsConfig() {
		return rankupsConfig;
	}
	
	/**
	 * get the effects handler to send effects to players
	 * @return null if the NMSHandler was not assigned, EffectsHandler instance otherwise
	 */
	public EffectsHandler getEffects() {
		return effects;
	}
	
	/**
	 * get the action handler which handles performing rankup actions
	 * @return RankupActionHandler instance
	 */
	public RankupActionHandler getActionHandler() {
		return rankupActionHandler;
	}
	
	/**
	 * get the placeholder replacer class which contains methods to parse placeholders for any String
	 * @return PlaceholderReplacer instance 
	 */
	public PlaceholderReplacer getPlaceholderReplacer() {
		return placeholderReplacer;
	}
	
	/**
	 * get the Vault economy class containing methods related to economy
	 * @return VaultEco class
	 */
	public VaultEco getEconomy() {
		return eco;
	}
	
	/**
	 * get the Vault permissions class containing methods related to permissions
	 * @return VaultPerms class
	 */
	public VaultPerms getPerms() {
		return perms;
	}
	
	/**
	 * get the Vault chat class containing methods related to chat
	 * @return VaultChat class
	 */
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
