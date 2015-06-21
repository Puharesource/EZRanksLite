package me.clip.ezrankslite.nms;

import java.util.List;

import org.bukkit.entity.Player;

public interface NMSHandler {

	/**
	 * send an actionbar message to a specific player
	 * @param p Player to send the actionbar to
	 * @param message message to send to the player
	 */
	public void sendActionbar(Player p, String message);
	
	/**
	 * broadcast an actionbar message to all players online
	 * @param message message to send to everyone
	 */
	public void broadcastActionbar(String message);
	
	/**
	 * send a json message to a specific player
	 * @param p Player to send the json message to
	 * @param message List<String> json message to send
	 */
	public void sendJSON(Player p, List<String> message);
	
	/**
	 * send a json message to all players online
	 * @param message List<String> json message to send
	 */
	public void broadcastJSON(List<String> message);
}
