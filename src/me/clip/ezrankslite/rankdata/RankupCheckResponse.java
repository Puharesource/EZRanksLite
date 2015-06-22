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
package me.clip.ezrankslite.rankdata;


public enum RankupCheckResponse {

	VALID("Valid"), 
	DUPLICATE_RANK_NAME("Already a rank loaded with this name"),
	NO_RANKUP("Does not contain a rankup name"), 
	NEGATIVE_ORDER("Has an incorrect order"),
	DUPLICATE_ORDER("Already a rankup loaded with the same order"), 
	NEGATIVE_COST("Has a negative cost"), 
	INVALID_COST("Has an invalid cost"), 
	NO_RANKUP_ACTIONS("No rankup actions listed"), 
	NO_RANK_PREFIX("No rank prefix specified");
	
	private String message;
	
	RankupCheckResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
