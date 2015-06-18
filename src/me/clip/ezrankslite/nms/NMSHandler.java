package me.clip.ezrankslite.nms;

import java.util.List;

import org.bukkit.entity.Player;

public interface NMSHandler {

	public void sendActionbar(Player p, String message);
	
	public void broadcastActionbar(String message);
	
	public void sendJSON(Player p, List<String> message);
	
	public void broadcastJSON(List<String> message);
}
