package me.clip.ezrankslite.listeners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.events.RankupEvent;
import me.clip.ezrankslite.util.EcoUtil;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RankupListener implements Listener {
	
	private EZRanksLite plugin;
	
	private SimpleDateFormat format;
	
	private File log = null;
	
	public RankupListener(EZRanksLite instance) {
		
		plugin = instance;
		
        log = new File(plugin.getDataFolder(), "rankup.log");
        
        if (!log.exists()) {
        	
            try {
                log.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to create rankups.log!");
                plugin.getLogger().warning("Rankups will not be logged to file!");
                return;
            }
        }
    
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
		format = new SimpleDateFormat("[MM/dd/yyyy HH:mm:ss]");
		
		plugin.getLogger().warning("rankup.log file created! Rankups will be logged to this file!");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onRankup(RankupEvent e) {
		
		if (log == null) {
			plugin.getLogger().warning("rankups.log was not found!");
			plugin.getLogger().warning("Unregistering rankup event listener!");
			RankupEvent.getHandlerList().unregister(this);
			return;
		}
		
		Date d = new Date();
		
		String time = format.format(d);
		
		 try {
			 
             PrintWriter writer = new PrintWriter(new FileWriter(log, true), true);
             
             writer.write(time + " " + e.getPlayerName() + " " + e.getOldRank() + " to " + e.getNewRank() + " cost:" + EcoUtil.format(e.getCost()));
             
             writer.write(System.getProperty("line.separator"));
             
             writer.close();
             
		} catch (IOException ex) {
			plugin.getLogger().warning("An error occurred while writing to the rankups.log file!");
			plugin.getLogger().info(time + " " + e.getPlayerName() + " old rank:" + e.getOldRank() + " new rank:" + e.getNewRank() + " cost:" + EcoUtil.format(e.getCost()));
		}
	}	
}
