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

import java.text.NumberFormat;
import java.util.Locale;

import me.clip.ezrankslite.MainConfig;

public class EcoUtil {

	public static double getDifference(double balance, double cost) {
		return cost-balance > 0 ? cost-balance : 0;
	}
	
	 public static String format(double d) {
		 
         NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
         
         format.setMaximumFractionDigits(2);
         
         format.setMinimumFractionDigits(0);
         
         return format.format(d);
     }
	 
	public static String getProgress(double balance, double cost) {

		float bal = (float) balance;

		float c =  (float) cost;

		float percent = (100 * bal) / c;

		int progress = (int) Math.floor(percent);

		if (progress >= 100) {
			return 100 + "%";
		} else if (progress < 0) {
			return 0 + "%";
			
		}
		
		return progress + "%";
		
	}
	
	public static String getProgressExact(double balance, double cost) {
		
		double percent = 100.0D * balance / cost;
		
		if (percent >= 100.0D) {
			return "100%";
		}
		
		if (percent < 0.0D) {
			return "0%";
		}
		
		String format = format(percent);

		return format + "%";
	}


	public static String fixMoney(double d) {

		if (d < 1000L) {
			return format(d);
		}
		if (d < 1000000L) {
			return format(d / 1000L) + MainConfig.getThousandsFormat();
		}
		if (d < 1000000000L) {
			return format(d / 1000000L) + MainConfig.getMillionsFormat();
		}
		if (d < 1000000000000L) {
			return format(d / 1000000000L) + MainConfig.getBillionsFormat();
		}
		if (d < 1000000000000000L) {
			return format(d / 1000000000000L) + MainConfig.getTrillionsFormat();
		}
		if (d < 1000000000000000000L) {
			return format(d / 1000000000000000L) + MainConfig.getQuadrillionsFormat();
		}

		long send = (long) d;
		return String.valueOf(send);
	}
}
