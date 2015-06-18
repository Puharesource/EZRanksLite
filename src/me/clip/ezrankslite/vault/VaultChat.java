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
package me.clip.ezrankslite.vault;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultChat {

	public VaultChat() {
	}
	
	private static Chat chat = null;
	
	public boolean hook() {
		if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			return false;
		}
		
		RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
		
		if (rsp == null) {
			return false;
		}
		
		chat = rsp.getProvider();
		
		return chat != null;
	}
	
	public void setPrefix(Player p, String prefix) {
		chat.setPlayerPrefix(p, prefix);
	}
	
	public void setPrefix(Player p, String prefix, String world) {
		chat.setPlayerPrefix(world, p, prefix);
	}
	
	public void setSuffix(Player p, String suffix) {
		chat.setPlayerSuffix(p, suffix);
	}
	
	public void setSuffix(Player p, String suffix, String world) {
		chat.setPlayerPrefix(world, p, suffix);
	}
	
}
