package me.clip.ezrankslite.listeners;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.rankdata.Rankup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
	
	private EZRanksLite plugin;
	
	public ChatListener(EZRanksLite instance) {
		plugin = instance;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		
		String format = e.getFormat();
		
		if (format.contains("{ezrankslite_rankprefix}")) {
			
			Rankup r = Rankup.getRankup(e.getPlayer());
			
			String pre = r != null ? r.getPrefix() : Rankup.isLastRank(e.getPlayer()) ? Rankup.getLastRank().getPrefix() : "";
			
			e.setFormat(format.replace("{ezrankslite_rankprefix}", ChatColor.translateAlternateColorCodes('&', pre)));
		}
	}
}
