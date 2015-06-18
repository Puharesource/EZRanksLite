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

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultPerms {

	public VaultPerms() {
	}
	
	private static Permission perms = null;

	public boolean hook() {
		
		if (!Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
			return false;
		}
		
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServer()
				.getServicesManager().getRegistration(Permission.class);
		
		if (rsp == null) {
			return false;
		}
		
		perms = rsp.getProvider();
		
		return perms != null;
	}
	
	public String getVaultVersion() {
		return Bukkit.getServer().getPluginManager().getPlugin("Vault").getDescription().getVersion();
	}
	
	public String[] getServerGroups() {
		return perms.getGroups();
	}

	public String[] getGroups(Player p) {
		return perms.getPlayerGroups(p);
	}
	
	public String getPrimaryGroup(Player p) {
		return perms.getPrimaryGroup(p);
	}
	
	public boolean hasPerm(Player p, String perm) {
		return perms.has(p, perm);
	}

	public boolean isValidGroup(String group) {
		boolean isValid = false;
		for (String g : getServerGroups()) {
			if (g.equals(group)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	public boolean addGroup(Player p, String group) {
		return perms.playerAddGroup(p, group);
	}
	
	public boolean addGroup(Player p, String group, String world) {
		return perms.playerAddGroup(world, p, group);
	}
	
	public boolean removeGroup(Player p, String group) {
		return perms.playerRemoveGroup(p, group);
	}
	
	public boolean removeGroup(Player p, String group, String world) {
		return perms.playerRemoveGroup(world, p, group);
	}
	
	public boolean addPermission(Player p, String permission) {
		return perms.playerAdd(p, permission);
	}
	
	public boolean addPermission(Player p, String permission, String world) {
		return perms.playerAdd(world, p, permission);
	}
	
	public boolean removePermission(Player p, String permission) {
		return perms.playerRemove(p, permission);
	}
	
	public boolean removePermission(Player p, String permission, String world) {
		return perms.playerRemove(world, p, permission);
	}
}
