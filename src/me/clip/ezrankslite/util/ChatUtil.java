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
