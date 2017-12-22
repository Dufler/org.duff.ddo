package org.duff.ddo.crafting;

public enum ESlot {
	
	HEADGEAR("Headgear"),
	GOOGLES("Googles"),
	NECKLACE("Necklace"),
	TRINKET("Trinket"),
	RING1("Ring 1"),
	RING2("Ring 2"),
	CLOAK("Cloak"),
	BELT("Belt"),
	BOOTS("Boots"),
	GLOVES("Gloves"),
	BRACERS("Bracers"),
	ARMOR("Armor"),
	WEAPON("Weapon"),
	SHIELD("Shield"),
	RUNEARM("Rune Arm");
	
	private final String description;
	
	private ESlot(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
