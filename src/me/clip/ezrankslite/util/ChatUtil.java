package me.clip.ezrankslite.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtil {

	/**
	 * send a message with color codes translated
	 * @param s CommandSender to send the message to
	 * @param message String message to send
	 */
	public static void msg(CommandSender s, String message) {
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	/**
	 * send a message with color codes translated
	 * @param s CommandSender to send the message to
	 * @param lines String[] lines to send
	 */
	public static void msg(CommandSender s, String[] lines) {
		for (String line : lines) {
			msg(s, line);
		}
	}
	
	/**
	 * broadcast a message to all players online with color codes translated
	 * @param message String message to send
	 */
	public static void broadcast(String message) {
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	/**
	 * broadcast a message to all players online with color codes translated
	 * @param lines String[] lines to send
	 */
	public static void broadcast(String[] lines) {
		for (String line : lines) {
			broadcast(line);
		}
	}
	
	/**
	 * split a String into a String[] at a newline character 
	 * @param message message to split into a String[]
	 * @return String[] for the split message
	 */
	public static String[] splitLines(String message) {
		
		String[] parts;
		
		if (message.contains("\n")) {
			
			parts = message.split("\n");
			
		} else {
			
			parts = new String[] { message };	
		}
		
		return parts;
	}
}
