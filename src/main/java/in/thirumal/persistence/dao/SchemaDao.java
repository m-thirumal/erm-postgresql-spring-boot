/**
 * 
 */
package in.thirumal.persistence.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.thirumal.model.Attribute;
import in.thirumal.model.Entity;
import in.thirumal.persistence.GenericDao;

/**
 * @author thirumal
 *
 */
@Repository
public class SchemaDao implements GenericDao<Entity> {

	private JdbcTemplate jdbcTemplate;
	DatabaseMetaData metadata;
	Connection connectionForAuto;
	
	
	@Autowired
	public SchemaDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Entity> list(String databaseName, String schemaName) {
		ResultSet resultSet = this.jdbcTemplate.execute(new ConnectionCallback<ResultSet>() {
	        @Override
	        public ResultSet doInConnection(Connection connection) throws SQLException {
	        	connectionForAuto = connection; 
	        	metadata = connection.getMetaData();
	            return connection.getMetaData().getColumns(databaseName, schemaName, null, null);
	        }
	    });
		return getEntities(resultSet, databaseName);
	}
	
	private List<Entity> getEntities(ResultSet resultSet, String dbName) {
		List<Entity> alTables = new ArrayList<>();
		Entity currentEntite = null;
		ArrayList<Attribute> alAttributs = null;
		String checkdenom = "";
		String tablename = null;
		String name = null;
		String type = null;
		Integer size = null;
		String tableSchem = null;
		String tablePrefix = null;
		ArrayList<String> pkColumnNames = new ArrayList<>();
		ArrayList<String> fkColumnNames = new ArrayList<>();
		try {
			while (resultSet.next()) {
				tableSchem = resultSet.getString("TABLE_SCHEM");
					if (!"sys".equalsIgnoreCase(tableSchem) && !"information_schema".equalsIgnoreCase(tableSchem)
						&& !"dbo".equalsIgnoreCase(tableSchem)) {
					tablename = resultSet.getString("TABLE_NAME");
					tablePrefix = resultSet.getString("TABLE_SCHEM");
					name = resultSet.getString("COLUMN_NAME");
					type = resultSet.getString("TYPE_NAME");
					size = resultSet.getInt("COLUMN_SIZE");
					if (!checkdenom.equals(tablename)) { // runs total number of table times
						// retrieve PKs
						pkColumnNames = new ArrayList<>();
						ResultSet rs = metadata.getPrimaryKeys(dbName, tablePrefix, tablename);
						while (rs.next()) {
							pkColumnNames.add(rs.getString("COLUMN_NAME"));
						}
						// Retrive FK
						fkColumnNames = new ArrayList<>();
						ResultSet rsFk = metadata.getImportedKeys(dbName, tablePrefix, tablename);
						while (rsFk.next()) {
							fkColumnNames.add(rsFk.getString("FKCOLUMN_NAME"));
						}
						currentEntite = new Entity(dbName, tablePrefix, tablename);
						if (tablePrefix.equalsIgnoreCase("Codes")) {
							currentEntite.setCodeTable(true);
							// Table Locale_Cd does not refer to
							// Codes.Locale_Locales
							if (!"Locale_Cd".equalsIgnoreCase(tablename)) {
								currentEntite.setConstantes(retrieveCdForConstantes(dbName, tablePrefix, tablename));
							}
						}
						alAttributs = new ArrayList<>();
						Attribute currentAttribut = new Attribute(name, type, size);
						for (String pkCol : pkColumnNames) {
							if (pkCol.equalsIgnoreCase(currentAttribut.getRawName())) {
								currentAttribut.setPrimaryKey(true);
								if (columnIsAutoincrement(dbName, tablePrefix, tablename, pkCol)) {
									currentAttribut.setAutoincrement(true);
								}
							}
						}
						for (String fkCol : fkColumnNames) {
							if (fkCol.equalsIgnoreCase(currentAttribut.getRawName())) {
								currentAttribut.setForeignKey(true);
							}
						}
						alAttributs.add(currentAttribut);
						currentEntite.setAlAttr(alAttributs);
						if (!currentEntite.getName().equalsIgnoreCase("RndView")) {
							alTables.add(currentEntite);
							checkdenom = tablename;
						}
					} else {
						// Creation du nouvel attribut
						Attribute currentAttribut = new Attribute(name, type, size);
						for (String pkCol : pkColumnNames) {
								if (pkCol.equalsIgnoreCase(currentAttribut.getRawName())) {
								currentAttribut.setPrimaryKey(true);
								if (columnIsAutoincrement(dbName, tablePrefix, tablename, pkCol)) {
									currentAttribut.setAutoincrement(true);
								}
							}
						}
						for (String fkCol : fkColumnNames) {
							if (fkCol.equalsIgnoreCase(currentAttribut.getRawName())) {
								currentAttribut.setForeignKey(true);
							}
						}
						alAttributs.add(currentAttribut);
						currentEntite.setAlAttr(alAttributs);
					}
				} 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alTables;
	}
	
	public boolean columnIsAutoincrement(String db, String tablePrefix, String tableName, String columnName) {
		boolean result = false;
		ResultSetMetaData rsMetadata = null;
		try (ResultSet rs = connectionForAuto.createStatement()
				.executeQuery("SELECT "+columnName+" FROM "+tablePrefix+"."+tableName)) {
			rs.next();
			rsMetadata = rs.getMetaData();
			result = rsMetadata.isAutoIncrement(1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public HashMap<String, Integer> retrieveCdForConstantes(String db, String tablePrefix, String tableName) {
		HashMap<String, Integer> result = new HashMap<>();	
		if(tableName.contains("_Cd")){
			String tableNameToLookup = tableName.replace("_Cd", "_Locales");
			//Cd description in english fetch only
			String query = "SELECT ["+tableName+"], [Description] FROM ["+db+"].[Codes].["+tableNameToLookup+"] WHERE [Locale_Cd] = 1";
			String key = null;
			Integer value = null;
			try (ResultSet rs = connectionForAuto.createStatement()
					.executeQuery(query)) {
				while(rs.next()){
					key = rs.getString("Description").toUpperCase().replaceAll(" ", "_").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", "")
							.replaceAll("-", "_").replaceAll("&", "").replaceAll("/", "").replaceAll("\\.", "_");
					key = StringUtils.stripAccents(key).replaceAll("[^a-zA-Z0-9_]", "_");
					//check if startsWithNumber
					//add underscore to conform to the Java namming allowed
					if(Character.isDigit(key.charAt(0))){
						key = "_"+key;
					}
					value = rs.getInt(tableName);
					result.put(key, value);
				}				
			} catch (SQLException ex) {
			}			
		}		
		return result;
	}	
	
}
