package org.duff.ddo.crafting.model;

import org.duff.ddo.crafting.ESlot;

public class Item {
	
	private final ESlot slot;
	
	private Property prefix;
	private Property suffix;
	private Property extra;
	
	public Item(ESlot slot) {
		this.slot = slot;
	}

	public Property getPrefix() {
		return prefix;
	}

	public void setPrefix(Property prefix) {
		this.prefix = prefix;
	}

	public Property getSuffix() {
		return suffix;
	}

	public void setSuffix(Property suffix) {
		this.suffix = suffix;
	}

	public Property getExtra() {
		return extra;
	}

	public void setExtra(Property extra) {
		this.extra = extra;
	}

	public ESlot getSlot() {
		return slot;
	}

	@Override
	public String toString() {
		String p = prefix != null ? prefix.getName() : "-";
		String s = suffix != null ? suffix.getName() : "-";
		String e = extra != null ? extra.getName() : "-";
		return "Item [slot = '" + slot + "', prefix = '" + p + "', suffix = '" + s + "', extra = '" + e + "']";
	}

}
