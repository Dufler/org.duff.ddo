package org.duff.ddo.crafting.model;

import java.util.HashSet;
import java.util.Set;

import org.duff.ddo.crafting.ESlot;

public class Property implements Comparable<Property> {
	
	public enum Group {
		
		ABILITY(0, "Ability"),
		ABSORPTION(1, "Absorption"),
		BANE(2, "Bane"),
		DAMAGE(3, "Damage"),
		DEFENSE(4, "Defense"),
		GUARD(5, "Guard"),
		MISC(6, "Misc"),
		OFFENSE(7, "Offense"),
		RESIST(8, "Resist"),
		SAVE(9, "Save"),
		SKILL(10, "Skill"),
		SPELL_CRITICAL(11, "Spell critical"),
		SPELLCASTING(12, "Spellcasting"),
		SPELLPOWER(13, "Spellpower"),
		STAT_DAMAGE(14, "Stat damage"),
		TACTICAL(15, "Tactical"),
		MISSING(16, "Missing");
		
		private final int id;
		private final String description;
		
		private Group(int id, String description) {
			this.id = id;
			this.description = description;
		}
		
		public int getID() {
			return id;
		}
		
		@Override
		public String toString() {
			return description;
		}

		public static Group getByID(int index) {
			Group group = null;
			for (Group g : values()) {
				if (g.getID() == index) {
					group = g;
					break;
				}
			}
			return group;
		}
	}
	
	public static final int MIN_PRIORITY = 0;
	public static final int MAX_PRIORITY = Integer.MAX_VALUE;
	
	private int priority;
	
	private int id;
	private String name;
	private Group group;
	
	private final Set<ESlot> prefix;
	private final Set<ESlot> suffix;
	private final Set<ESlot> extra;
	
	public Property() {
		prefix = new HashSet<>();
		suffix = new HashSet<>();
		extra = new HashSet<>();
		setPriority(MIN_PRIORITY);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void addPrefix(ESlot slot) {
		prefix.add(slot);
	}
	
	public Set<ESlot> getPrefix() {
		return prefix;
	}
	
	public void resetPrefix() {
		prefix.clear();
	}
	
	public void addSuffix(ESlot slot) {
		suffix.add(slot);
	}
	
	public Set<ESlot> getSuffix() {
		return suffix;
	}
	
	public void resetSuffix() {
		suffix.clear();
	}
	
	public void addExtra(ESlot slot) {
		extra.add(slot);
	}
	
	public Set<ESlot> getExtra() {
		return extra;
	}
	
	public void resetExtra() {
		extra.clear();
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		if (priority < MIN_PRIORITY)
			priority = MIN_PRIORITY;
		this.priority = priority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Property [priority=" + priority + ", id=" + id + ", name=" + name + ", prefix=" + prefix + ", suffix="
				+ suffix + ", extra=" + extra + "]";
	}

	@Override
	public int compareTo(Property p) {
		Integer p1 = priority;
		Integer p2 = p.getPriority();
		int compare = p1.compareTo(p2) * -1;
		return compare;
	}

}
