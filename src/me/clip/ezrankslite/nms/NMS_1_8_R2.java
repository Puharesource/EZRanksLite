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
