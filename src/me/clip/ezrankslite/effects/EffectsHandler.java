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
package me.clip.ezrankslite.effects;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;


public class EffectsHandler {
	
	public EffectsHandler() {
		
	}
	
	/**
	 * play an effect at a specific players location
	 * @param p Player to play the effect for
	 * @param effectName Name of the effect to play
	 */
	public void playEffect(Player p, String effectName) {
		
		if (p == null) {
			return;
		}
		
		effectName = effectName.toLowerCase();
		
		Location loc = p.getLocation();
		
		switch (effectName) {
		case "explosion": 
		case "explode":
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.EXPLOSION, 0, 0, 1, 2, 1, 0, 12, 2);
			break;
		case "largeexplosion":
		case "largeexplode":
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.EXPLOSION_LARGE, 1, 0, 1, 2, 1, 0, 12, 2);
			break;
		case "hugeexplosion":
		case "hugeexplode":
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.EXPLOSION_HUGE, 2, 0, 1, 2, 1, 0, 12, 2);
			break;
		case "fireworkspark":
		case "fireworksspark":
		case "spark":
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.FIREWORKS_SPARK, 3, 0, 1, 2, 1, 0, 12, 2);
			break;
		case "firework":
		case "fireworks":
			fireworks(loc);
			break;
		case "watersplash":
		case "splash":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.SPLASH, 5, 0, 0, 1, 0, 1, 12, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.SPLASH, 5, 0, 1, 2, 1, 0, 12, 2);
			break;
		case "potion":
		case "potionswirl":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.POTION_SWIRL, 0, 0, 0, 1, 0, 1, 12, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.POTION_SWIRL, 0, 0, 1, 2, 1, 0, 12, 2);
			break;
		case "heart":
		case "hearts":
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.HEART, 34, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "dust":
		case "coloreddust":
		case "coloureddust":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.COLOURED_DUST, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.COLOURED_DUST, 0, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "note":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.NOTE, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.NOTE, 0, 0, 1, 2, 1, 0, 12, 2);
			break;
		case "villager":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.VILLAGER_THUNDERCLOUD, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.VILLAGER_THUNDERCLOUD, 0, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "crit":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.CRIT, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.CRIT, 0, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "ender":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.ENDER_SIGNAL, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 0, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "extinguish":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.EXTINGUISH, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.EXTINGUISH, 0, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "flame":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.FLAME, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.FLAME, 0, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "glyph":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.FLYING_GLYPH, 0, 0, 0, 1, 0, 1, 50, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.FLYING_GLYPH, 0, 0, 1, 2, 1, 0, 50, 2);
			break;
		case "spell":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.SPELL, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.SPELL, 0, 0, 1, 2, 1, 0, 6, 2);
			break;			
		case "large_smoke":
		case "largesmoke":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.LARGE_SMOKE, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 0, 0, 1, 2, 1, 0, 6, 2);
			break;	
		case "lavapop":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.LAVA_POP, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.LAVA_POP, 0, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "lavadrip":
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.LAVADRIP, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.LAVADRIP, 0, 0, 1, 2, 1, 0, 6, 2);
			break;
		case "spawner_flames":
		case "spawnerflames":
			
			p.getWorld().spigot().playEffect(p.getLocation().add(0, 1, 0), Effect.MOBSPAWNER_FLAMES, 0, 0, 0, 1, 0, 1, 6, 2);
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 0, 0, 1, 2, 1, 0, 6, 2);
			break;	
		}
	}
				
	private void fireworks(Location loc) {
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		Random r = new Random();
		int rt = r.nextInt(4) + 1;
		FireworkEffect.Type type = FireworkEffect.Type.BALL;
		if (rt == 1)
			type = FireworkEffect.Type.BALL;
		if (rt == 2)
			type = FireworkEffect.Type.BALL_LARGE;
		if (rt == 3)
			type = FireworkEffect.Type.BURST;
		if (rt == 4)
			type = FireworkEffect.Type.CREEPER;
		if (rt == 5)
			type = FireworkEffect.Type.STAR;

		int r1 = r.nextInt(256);
		int b = r.nextInt(256);
		int g = r.nextInt(256);
		Color c1 = Color.fromRGB(r1, g, b);

		r1 = r.nextInt(256);
		b = r.nextInt(256);
		g = r.nextInt(256);
		Color c2 = Color.fromRGB(r1, g, b);
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		fwm.setPower(1);
		fw.setFireworkMeta(fwm);
		System.out.println("fireworks");
	}

}