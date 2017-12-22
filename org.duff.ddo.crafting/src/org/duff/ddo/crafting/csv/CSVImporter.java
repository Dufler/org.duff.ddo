package org.duff.ddo.crafting.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.duff.ddo.crafting.ESlot;
import org.duff.ddo.crafting.data.PropertyDAO;
import org.duff.ddo.crafting.model.Property;
import org.duff.ddo.crafting.model.Property.Group;

public class CSVImporter {
	
	private static final String GROUP_COLUMN = "group";
	private static final String NAME_COLUMN = "name";
	private static final String PREFIX_COLUMN = "prefix";
	private static final String SUFFIX_COLUMN = "suffix";
	private static final String EXTRA_COLUMN = "extra";
	
	private final PropertyDAO dao;
	
	public CSVImporter() {
		dao = PropertyDAO.getInstance();
	}
	
	public void importCSV() {
		try {
			File file = new File("C:/Users/DuflerSSD/Documents/Craft.csv");
			FileReader reader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(reader);
			String line = bReader.readLine();
			checkCSV(line);
			line = bReader.readLine();
			while (line != null) {
				String[] values = line.split(";", 5);
				addNewProperty(values);
				line = bReader.readLine();
			}
			bReader.close();
		} catch (IOException e) {
			System.out.println("Errori di IO durante la lettura del file csv");
		}
	}
	
	private void checkCSV(String firstLine) {
		if (firstLine == null || firstLine.isEmpty())
			throw new IllegalArgumentException("Il file csv è vuoto.");
		String[] columnNames = firstLine.split(";", 5);
		if (!GROUP_COLUMN.equals(columnNames[0]))
				throw new IllegalArgumentException("La prima colonna del csv deve contenere i group");
		if (!NAME_COLUMN.equals(columnNames[1]))
			throw new IllegalArgumentException("La seconda colonna del csv deve contenere i nomi");
		if (!PREFIX_COLUMN.equals(columnNames[2]))
			throw new IllegalArgumentException("La prima colonna del csv deve contenere i prefix");
		if (!SUFFIX_COLUMN.equals(columnNames[3]))
			throw new IllegalArgumentException("La prima colonna del csv deve contenere i suffix");
		if (!EXTRA_COLUMN.equals(columnNames[4]))
			throw new IllegalArgumentException("La prima colonna del csv deve contenere gli extra");
	}
	
	private void addNewProperty(String[] values) {
		Property property = new Property();
		Group group = Group.valueOf(values[0].toUpperCase().trim());
		property.setGroup(group);
		property.setName(values[1].trim());
		String[] prefixes = values[2].split(",");
		for (String prefix : prefixes) {
			if (!prefix.isEmpty()) {
				ESlot slot = ESlot.valueOf(prefix.toUpperCase().trim());
				property.addPrefix(slot);
			}
		}
		String[] suffixes = values[3].split(",");
		for (String suffix : suffixes) {
			if (!suffix.isEmpty()) {
				ESlot slot = ESlot.valueOf(suffix.toUpperCase().trim());
				property.addSuffix(slot);
			}
		}
		String[] extras = values[4].split(",");
		for (String extra : extras) {
			if (!extra.isEmpty()) {
				ESlot slot = ESlot.valueOf(extra.toUpperCase().trim());
				property.addExtra(slot);
			}
		}
		dao.saveProperty(property);
	}

}
