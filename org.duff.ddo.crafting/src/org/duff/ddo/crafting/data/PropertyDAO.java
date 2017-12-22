package org.duff.ddo.crafting.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.duff.ddo.crafting.ESlot;
import org.duff.ddo.crafting.model.Item;
import org.duff.ddo.crafting.model.Property;
import org.duff.ddo.crafting.model.Property.Group;

public class PropertyDAO {

	private static final String CONNECTION_STRING = "jdbc:sqlite:ddoCrafting.db";
	private static final String TABLE_NAME = "crafting_property";
	private static final String SLOT_SEPARATOR = "-";
	
	private static PropertyDAO instance;

	private final List<Property> properties;
	
	private PropertyDAO() {
		properties = new LinkedList<>();
		initDB();
		loadProperties();
	}

	public static PropertyDAO getInstance() {
		if (instance == null) {
			instance = new PropertyDAO();
		}
		return instance;
	}

	public Property[] getProperties() {
		Property[] copy = new Property[properties.size()];
		copy = properties.toArray(copy);
		return copy;
	}

	private void initDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(CONNECTION_STRING);
			Statement statement = connection.createStatement();
			String creationSql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME	+ " (id integer PRIMARY KEY, name string, type string, prefix string, suffix string, extra string);";
			statement.executeUpdate(creationSql);
			System.out.println("DB ready.");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Property> loadProperties() {
		properties.clear();
		try {
			Connection connection = DriverManager.getConnection(CONNECTION_STRING);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from " + TABLE_NAME);
			while (rs.next()) {
				Property property = new Property();
				property.setId(rs.getInt("id"));
				property.setName(rs.getString("name"));
				property.setGroup(Group.valueOf(rs.getString("type")));
				//Prefix
				String prefix = rs.getString("prefix");
				if (!prefix.isEmpty()) {
					String[] prefixes = prefix.split(SLOT_SEPARATOR);
					for (String p : prefixes) {
						ESlot slot = ESlot.valueOf(p);
						property.addPrefix(slot);
					}
				}
				//Suffix
				String suffix = rs.getString("suffix");
				if (!suffix.isEmpty()) {
					String[] suffixes = suffix.split(SLOT_SEPARATOR);
					for (String s : suffixes) {
						ESlot slot = ESlot.valueOf(s);
						property.addSuffix(slot);
					}
				}
				//Prefix
				String extra = rs.getString("extra");
				if (!extra.isEmpty()) {
					String[] extras = extra.split(SLOT_SEPARATOR);
					for (String e : extras) {
						ESlot slot = ESlot.valueOf(e);
						property.addExtra(slot);
					}
				}
				properties.add(property);
				System.out.println("Loaded " + property);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + properties.size() + " properties.");
		return properties;
	}

	public boolean saveProperty(Property property) {
		boolean insert;
		try {
			Connection connection = DriverManager.getConnection(CONNECTION_STRING);
			String sqlInsert = "INSERT INTO " + TABLE_NAME + "(name, type, prefix, suffix, extra) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, property.getName());
			statement.setString(2, property.getGroup().name());
			statement.setString(3, getSlot(property.getPrefix()));
			statement.setString(4, getSlot(property.getSuffix()));
			statement.setString(5, getSlot(property.getExtra()));
			int rows = statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next() && rows == 1) {
				int id = generatedKeys.getInt(1);
				property.setId(id);
				properties.add(property);
				insert = true;
				System.out.println("Property saved: " + property);
			} else {
				insert = false;
				System.out.println("Insertion failed");
			}
		} catch (SQLException e) {
			insert = false;
			e.printStackTrace();
		}
		return insert;
	}

	public boolean updateProperty(Property property) {
		boolean update;
		try {
			Connection connection = DriverManager.getConnection(CONNECTION_STRING);
			String sqlUpdate = "UPDATE " + TABLE_NAME + " SET name = ?, type = ?, prefix = ?, suffix = ?, extra = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sqlUpdate);
			statement.setString(1, property.getName());
			statement.setString(2, property.getGroup().name());
			statement.setString(3, getSlot(property.getPrefix()));
			statement.setString(4, getSlot(property.getSuffix()));
			statement.setString(5, getSlot(property.getExtra()));
			statement.setInt(6, property.getId());
			int rows = statement.executeUpdate();
			update = rows == 1;
			if (update)
				System.out.println("Property updated: " + property);
			else
				System.out.println("Update failed");
		} catch (SQLException e) {
			update = false;
			e.printStackTrace();
		}
		return update;
	}
	
	public boolean deleteProperty(Property property) {
		boolean delete;
		try {
			Connection connection = DriverManager.getConnection(CONNECTION_STRING);
			String sqlDelete = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sqlDelete);
			statement.setInt(1, property.getId());
			int rows = statement.executeUpdate();
			delete = rows == 1;
			if (delete) {
				properties.remove(property);
				System.out.println("Property deleted: " + property);
			} else {
				System.out.println("Deletion failed");
			}
		} catch (SQLException e) {
			delete = false;
			e.printStackTrace();
		}
		return delete;
	}
	
	private String getSlot(Set<ESlot> set) {
		String slots = "";
		for (ESlot slot : set) {
			slots += slot.name() + SLOT_SEPARATOR;
		}
		if (!slots.isEmpty())
			slots = slots.substring(0, slots.length() - 1);
		return slots;
	}

	public List<Property> getAvaiblePropertiesForPrefix(Item item) {
		List<Property> avaibleProperties = new ArrayList<>();
		for (Property property : getProperties()) {
			if (property.getPrefix().contains(item.getSlot()))
				avaibleProperties.add(property);
		}
		return avaibleProperties;
	}

	public List<Property> getAvaiblePropertiesForSuffix(Item item) {
		List<Property> avaibleProperties = new ArrayList<>();
		for (Property property : getProperties()) {
			if (property.getSuffix().contains(item.getSlot()))
				avaibleProperties.add(property);
		}
		return avaibleProperties;
	}

	public List<Property> getAvaiblePropertiesForExtra(Item item) {
		List<Property> avaibleProperties = new ArrayList<>();
		for (Property property : getProperties()) {
			if (property.getExtra().contains(item.getSlot()))
				avaibleProperties.add(property);
		}
		return avaibleProperties;
	}

}
