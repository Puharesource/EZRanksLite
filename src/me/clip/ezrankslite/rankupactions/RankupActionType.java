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
package me.clip.ezrankslite.rankupactions;

public enum RankupActionType {
	
	CONSOLE_COMMAND("[consolecommand]"), 
	PLAYER_COMMAND("[playercommand]"),
	MESSAGE("[message]"), 
	BROADCAST("[broadcast]"),
	JSON_MESSAGE("[jsonmessage]"), 
	JSON_BROADCAST("[jsonbroadcast]"), 
	ACTIONBAR_MESSAGE("[actionbarmessage]"), 
	ACTIONBAR_BROADCAST("[actionbarbroadcast]"),
	ADD_GROUP("[addgroup]"),
	REMOVE_GROUP("[removegroup]"),
	ADD_PERMISSION("[addpermission]"),
	REMOVE_PERMISSION("[removepermission]"),
	SET_PREFIX("[setprefix]"), 
	SET_SUFFIX("[setsuffix]"),
	EFFECT("[effect]"),
	SOUND("[sound]");
	
	private String identifier;
	
	RankupActionType(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public static RankupActionType fromIdentifier(String identifier){
		for(RankupActionType type : values()){
			if (type.getIdentifier().equalsIgnoreCase(identifier)) {
				return type;
			}
		}
		return null;
	}

}
