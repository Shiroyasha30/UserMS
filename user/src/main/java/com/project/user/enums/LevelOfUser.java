package com.project.user.enums;

import lombok.Getter;

@Getter
public enum LevelOfUser {
	HIGH(40),
	HIGH_INTERMEDIATE(30),
	INTERMEDIATE(20),
	LOW_INTERMEDIATE(10),
	LOW(0);
	
	int level;

	private LevelOfUser(int level) {
		this.level = level;
	}
	
	public static LevelOfUser findLevelOfUser(int lvl) {
		for(LevelOfUser e : values()) {
			if(e.level == lvl) {
				return e;
			}
		}
		return null;
	}
}
