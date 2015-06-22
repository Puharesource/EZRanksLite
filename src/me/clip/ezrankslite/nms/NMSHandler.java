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
