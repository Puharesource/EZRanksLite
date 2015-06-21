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
package me.clip.ezrankslite.multipliers;

import java.util.TreeMap;

import org.bukkit.entity.Player;

import me.clip.ezrankslite.EZRanksLite;

public class CostHandler {

	EZRanksLite plugin;

	public CostHandler(EZRanksLite i) {
		plugin = i;
	}
    //                       priority, discount object that holds permission and discount
	protected static TreeMap<Integer, Discount> discounts = new TreeMap<Integer, Discount>();
	protected static TreeMap<Integer, Multiplier> multipliers = new TreeMap<Integer, Multiplier>();
	
	public static void unload() {
		discounts = null;
		multipliers = null;
	}
	
	/**
	 * apply a discount to the provided cost if a player has access to a discount
	 * @param p Player to apply the discount for
	 * @param cost cost to apply the discount to
	 * @return new cost with the discount applied if a player had access to a discount, provided cost if no discount was applied
	 */
	public static double getDiscount(Player p, double cost) {

		if (discounts == null || discounts.isEmpty()) {
			return cost;
		}

		for (int i : discounts.keySet()) {

			String perm = discounts.get(i).getPermission();

			if (p.hasPermission(perm)) {

				double d = (discounts.get(i).getMultiplier() / 100) * cost;

				if (cost - d >= 1) {
					return cost - d;
				}

				return 1;

			}
		}
		return cost;
	}

	/**
	 * apply a cost multiplier to a specific cost if a player has access to a cost multiplier
	 * @param p Player to apply the multiplier to
	 * @param cost cost to apply the the multiplier to
	 * @return updated cost with multiplier set based on the cost provided if a player has a cost multiplier, normal cost otherwise
	 */
	public static double getMultiplier(Player p, double cost) {

		if (multipliers == null || multipliers.isEmpty()) {
			return cost;
		}

		for (int i : multipliers.keySet()) {

			String perm = multipliers.get(i).getPermission();

			if (p.hasPermission(perm)) {

				double d = (multipliers.get(i).getMultiplier() / 100) * cost;

				return cost + d;

			}
		}
		return cost;
	}

}
