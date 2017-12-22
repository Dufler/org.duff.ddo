package org.duff.ddo.gui.composite;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.duff.ddo.crafting.ESlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class PropertySlotComposite extends Group {
	
	public enum Type {
		
		NONE(""),
		PREFIX("Prefix"),
		SUFFIX("Suffix"),
		EXTRA("Extra");
		
		private final String title;
		
		private Type(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
		
	}

	private final Type type;
	private final HashMap<ESlot, Button> mappaBottoni;
	
	private Button btnHeadgear;
	private Button btnGoogles;
	private Button btnRing;
	private Button btnSecondRing;
	private Button btnBelt;
	private Button btnArmor;
	private Button btnNecklace;
	private Button btnBracers;
	private Button btnCloak;
	private Button btnWeapon;
	private Button btnAmulet;
	private Button btnGloves;
	private Button btnBoots;
	private Button btnShield;
	private Button btnRuneArm;
	
	public PropertySlotComposite(Composite parent, Type type) {
		super(parent, SWT.NONE);
		setText(type.toString());
		setLayout(new GridLayout(4, false));
		
		this.type = type;
		mappaBottoni = new HashMap<>();
		
		btnHeadgear = new Button(this, SWT.CHECK);
		btnHeadgear.setText(ESlot.HEADGEAR.toString());
		mappaBottoni.put(ESlot.HEADGEAR, btnHeadgear);
		
		btnGoogles = new Button(this, SWT.CHECK);
		btnGoogles.setText(ESlot.GOOGLES.toString());
		mappaBottoni.put(ESlot.GOOGLES, btnGoogles);
		
		btnRing = new Button(this, SWT.CHECK);
		btnRing.setText("Ring");
		mappaBottoni.put(ESlot.RING1, btnRing);
		
		if (type == Type.NONE) {
			btnRing.setText(ESlot.RING1.toString());
			btnSecondRing = new Button(this, SWT.CHECK);
			btnSecondRing.setText(ESlot.RING2.toString());
			mappaBottoni.put(ESlot.RING2, btnSecondRing);
		}		
		
		btnBelt = new Button(this, SWT.CHECK);
		btnBelt.setText(ESlot.BELT.toString());
		mappaBottoni.put(ESlot.BELT, btnBelt);
		
		btnArmor = new Button(this, SWT.CHECK);
		btnArmor.setText(ESlot.ARMOR.toString());
		mappaBottoni.put(ESlot.ARMOR, btnArmor);
		
		btnNecklace = new Button(this, SWT.CHECK);
		btnNecklace.setText(ESlot.NECKLACE.toString());
		mappaBottoni.put(ESlot.NECKLACE, btnNecklace);
		
		btnBracers = new Button(this, SWT.CHECK);
		btnBracers.setText(ESlot.BRACERS.toString());
		mappaBottoni.put(ESlot.BRACERS, btnBracers);
		
		btnCloak = new Button(this, SWT.CHECK);
		btnCloak.setText(ESlot.CLOAK.toString());
		mappaBottoni.put(ESlot.CLOAK, btnCloak);
		
		btnWeapon = new Button(this, SWT.CHECK);
		btnWeapon.setText(ESlot.WEAPON.toString());
		mappaBottoni.put(ESlot.WEAPON, btnWeapon);
		
		btnAmulet = new Button(this, SWT.CHECK);
		btnAmulet.setText(ESlot.TRINKET.toString());
		mappaBottoni.put(ESlot.TRINKET, btnAmulet);
		
		btnGloves = new Button(this, SWT.CHECK);
		btnGloves.setText(ESlot.GLOVES.toString());
		mappaBottoni.put(ESlot.GLOVES, btnGloves);
		
		btnBoots = new Button(this, SWT.CHECK);
		btnBoots.setText(ESlot.BOOTS.toString());
		mappaBottoni.put(ESlot.BOOTS, btnBoots);
		
		btnShield = new Button(this, SWT.CHECK);
		btnShield.setText(ESlot.SHIELD.toString());
		mappaBottoni.put(ESlot.SHIELD, btnShield);
		
		btnRuneArm = new Button(this, SWT.CHECK);
		btnRuneArm.setText(ESlot.RUNEARM.toString());
		mappaBottoni.put(ESlot.RUNEARM, btnRuneArm);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void setSelectedSlots(Set<ESlot> slots) {
		for (Button bottone : mappaBottoni.values()) {
			bottone.setSelection(false);
		}
		for (ESlot slot : slots) {
			Button bottone = mappaBottoni.get(slot);
			if (bottone != null)
				bottone.setSelection(true);
		}
	}
	
	public Set<ESlot> getSelectedSlots() {
		Set<ESlot> slots = new HashSet<>();
		for (ESlot slot : mappaBottoni.keySet()) {
			Button bottone = mappaBottoni.get(slot);
			if (bottone.getSelection())
				slots.add(slot);
		}
		if (type != Type.NONE && slots.contains(ESlot.RING1))
			slots.add(ESlot.RING2);
		return slots;
	}

//	public boolean getBtnHeadgear() {
//		return btnHeadgear.getSelection();
//	}
//
//	public void setBtnHeadgear(boolean headgear) {
//		btnHeadgear.setSelection(headgear);
//	}
//
//	public boolean getBtnRing() {
//		return btnRing.getSelection();
//	}
//
//	public void setBtnRing(boolean ring) {
//		btnRing.setSelection(ring);
//	}
//	
//	public boolean getBtnSecondRing() {
//		return btnSecondRing.getSelection();
//	}
//
//	public void setBtnSecondRing(boolean ring) {
//		btnSecondRing.setSelection(ring);
//	}
//
//	public boolean getBtnBelt() {
//		return btnBelt.getSelection();
//	}
//
//	public void setBtnBelt(boolean belt) {
//		btnBelt.setSelection(belt);;
//	}
//
//	public boolean getBtnArmor() {
//		return btnArmor.getSelection();
//	}
//
//	public void setBtnArmor(boolean armor) {
//		btnArmor.setSelection(armor);
//	}
//
//	public boolean getBtnNecklace() {
//		return btnNecklace.getSelection();
//	}
//
//	public void setBtnNecklace(boolean necklace) {
//		btnNecklace.setSelection(necklace);
//	}
//
//	public boolean getBtnBracers() {
//		return btnBracers.getSelection();
//	}
//
//	public void setBtnBracers(boolean bracers) {
//		btnBracers.setSelection(bracers);
//	}
//
//	public boolean getBtnCloak() {
//		return btnCloak.getSelection();
//	}
//
//	public void setBtnCloak(boolean cloak) {
//		btnCloak.setSelection(cloak);
//	}
//
//	public boolean getBtnWeapon() {
//		return btnWeapon.getSelection();
//	}
//
//	public void setBtnWeapon(boolean weapon) {
//		btnWeapon.setSelection(weapon);
//	}
//
//	public boolean getBtnAmulet() {
//		return btnAmulet.getSelection();
//	}
//
//	public void setBtnAmulet(boolean amulet) {
//		btnAmulet.setSelection(amulet);
//	}
//
//	public boolean getBtnGloves() {
//		return btnGloves.getSelection();
//	}
//
//	public void setBtnGloves(boolean gloves) {
//		btnGloves.setSelection(gloves);
//	}
//
//	public boolean getBtnBoots() {
//		return btnBoots.getSelection();
//	}
//
//	public void setBtnBoots(boolean boots) {
//		btnBoots.setSelection(boots);
//	}
//
//	public boolean getBtnShield() {
//		return btnShield.getSelection();
//	}
//
//	public void setBtnShield(boolean shield) {
//		btnShield.setSelection(shield);
//	}

}
