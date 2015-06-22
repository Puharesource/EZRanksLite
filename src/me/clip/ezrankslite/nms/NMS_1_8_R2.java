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

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMS_1_8_R2 implements NMSHandler {

	@Override
	public void sendActionbar(Player p, String message) {
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \""+ChatColor.translateAlternateColorCodes('&', message)+"\"}");
		PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
	}

	@Override
	public void broadcastActionbar(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendActionbar(p, message);
		}
	}

	@Override
	public void sendJSON(Player p, List<String> json) {


		if (json == null || json.isEmpty()) {
			return;
		}

		String first = json.get(0);

		try {
			IChatBaseComponent icbc = ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', first));

			for (String line : json) {

				if (line == null || line.isEmpty() || line.equals(first)) {
					continue;
				}

				icbc.addSibling(ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', line)));
			}

			PacketPlayOutChat chat = new PacketPlayOutChat(icbc, (byte) 0);

			((CraftPlayer) p).getHandle().playerConnection.sendPacket(chat);

		} catch (Exception e) {
			System.out.println("[EZRanksLite] There was an error sending the following JSON message to: "
							+ p != null ? p.getName() : "unknown player");
			System.out.println("[EZRanksLite] Message: " + json != null ? json.toString() : "null");
			System.out.println("[EZRanksLite] Reason: " + e.getMessage() != null ? e.getMessage() : "unknown error");
		}
	
	}

	@Override
	public void broadcastJSON(List<String> message) {
		for (Player p : Bukkit.getOnlinePlayers()){
			sendJSON(p, message);
		}
	}

}
