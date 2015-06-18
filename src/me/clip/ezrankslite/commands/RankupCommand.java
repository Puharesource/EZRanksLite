package me.clip.ezrankslite.commands;

import java.util.HashMap;
import java.util.Map;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.Lang;
import me.clip.ezrankslite.MainConfig;
import me.clip.ezrankslite.events.RankupEvent;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.rankdata.Rankup;
import me.clip.ezrankslite.util.ChatUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand implements CommandExecutor {
	
	private EZRanksLite plugin;
	
	private static Map<String, Integer> toConfirm;
	
	private static boolean confirmToRankup;
	
	private final int confirmTime;
	
	private static Map<String, Long> cooldown;
	
	public RankupCommand(EZRanksLite instance) {
		
		plugin = instance;
		
		plugin.getCommand("rankup").setExecutor(this);
		
		if (plugin.getConfig().getBoolean("confirm_to_rankup.enabled")) {
			
			confirmToRankup = true;
			
			toConfirm = new HashMap<String, Integer>();
			
			confirmTime = plugin.getConfig().getInt("confirm_to_rankup.time");
			
		} else {
			
			confirmTime = 10;
		}
		
		if (MainConfig.rankupCooldownEnabled()) {
			cooldown = new HashMap<String, Long>();
		}
	}
	
	public static void unload() {
		toConfirm = null;
		cooldown = null;
	}
	
	public static void setConfirmToRankup(boolean b) {
		
		confirmToRankup = b;
		
		if (!b) {
			toConfirm = null;
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

		if (!(s instanceof Player)) {
			
			return true;
		}
		
		Player p = (Player) s;
		
		if (!p.hasPermission("ezranks.rankup")) {
			//no perms
			ChatUtil.msg(p, Lang.NO_PERMISSION.getConfigValue(new String[] {
					"ezranks.rankup"
			}));
			return true;
		}
		
		Rankup r = Rankup.getRankup(p);
		
		if (r == null) {
			
			if (Rankup.isLastRank(p)) {
				
				String msg = Lang.RANKUP_LAST_RANK.getConfigValue(new String[] {
						Rankup.getLastRank().getRank() != null ? Rankup.getLastRank().getRank() : ""
				});
				
				msg = plugin.getPlaceholderReplacer().setPlaceholders(p, null, msg);
				
				String[] parts = ChatUtil.splitLines(msg);
				
				ChatUtil.msg(p, parts);
				
			} else {
			
				String msg = Lang.RANKUP_NO_RANKUPS.getConfigValue(new String[] {
						plugin.getPerms().getPrimaryGroup(p) != null ? plugin.getPerms().getPrimaryGroup(p) : ""
				});
				
				msg = plugin.getPlaceholderReplacer().setPlaceholders(p, null, msg);
				
				String[] parts = ChatUtil.splitLines(msg);
				
				ChatUtil.msg(p, parts);
			}
			
			return true;
		}
		
		if (confirmToRankup) {
			
			if (toConfirm == null) {
				toConfirm = new HashMap<String, Integer>();
			}
			
			if (!toConfirm.containsKey(p.getName())) {
				
				String confirmMsg = Lang.RANKUP_CONFIRM.getConfigValue(new String[] {
						r.getRank(), r.getPrefix(), String.valueOf(r.getCost())
				});
				
				confirmMsg = plugin.getPlaceholderReplacer().setPlaceholders(p, r, confirmMsg);
				
				String[] parts = ChatUtil.splitLines(confirmMsg);
				
				ChatUtil.msg(p, parts);
				
				final String name = p.getName();

				int id = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						toConfirm.remove(name);
					}
				}, 20L*confirmTime).getTaskId();
				
				toConfirm.put(name, id);
				
				return true;
			} else {
				Bukkit.getScheduler().cancelTask(toConfirm.get(p.getName()));
			}
			
			toConfirm.remove(p.getName());
		}
		
		double cost = r.getCost();
		
		cost = CostHandler.getMultiplier(p, cost);

		cost = CostHandler.getDiscount(p, cost);
		
		double balance = plugin.getEconomy().getBalance(p);
		
		if (balance < cost) {
			
			for (String line : Rankup.getRequirementMessage()) {
				
				line = plugin.getPlaceholderReplacer().setPlaceholders(p, r, line);
				
				ChatUtil.msg(p, line);
			}
			
			return true;
		}
		
		if (MainConfig.rankupCooldownEnabled()) {
			
			if (cooldown == null) {
				cooldown = new HashMap<String, Long>();
			}
			
			long now = System.currentTimeMillis();
			
			if (cooldown.containsKey(p.getName())) {
				
				long expireTime = cooldown.get(p.getName());
				
				if (now < expireTime) {
					
					String cooldown = Lang.RANKUP_ON_COOLDOWN.getConfigValue(null);
					
					int timeLeft = (int) ((expireTime-now)/1000);
					
					cooldown = cooldown.replace("%time%", String.valueOf(timeLeft));
					
					cooldown = plugin.getPlaceholderReplacer().setPlaceholders(p, r, cooldown);
					
					String[] parts = ChatUtil.splitLines(cooldown);
					
					ChatUtil.msg(p, parts);
					
					return true;
				}
			}
			
			cooldown.put(p.getName(), now + MainConfig.rankupCooldownTime());
		}
		
		RankupEvent event = new RankupEvent(p, r.getRank(), r.getRankup(), cost);
		
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled()) {
			plugin.debug(false, "Rankup event for " + p.getName() + " was cancelled");
			return true;
		}
		
		plugin.getActionHandler().executeRankupActions(p, r);
		
		plugin.getEconomy().withdrawMoney(cost, p);
		
		return true;
	}
}
