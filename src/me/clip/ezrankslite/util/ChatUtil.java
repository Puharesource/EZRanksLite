package me.clip.ezrankslite.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtil {

	public static void msg(CommandSender s, String message) {
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public static void msg(CommandSender s, String[] lines) {
		for (String line : lines) {
			msg(s, line);
		}
	}
	
	public static void broadcast(String message) {
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public static void broadcast(String[] lines) {
		for (String line : lines) {
			broadcast(line);
		}
	}
	
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
