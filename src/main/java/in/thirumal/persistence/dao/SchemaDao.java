/**
 * 
 */
package in.thirumal.persistence.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	            return connection.getMetaData().getColumns(databaseName, schemaName, null, null);
	        }
	    });
		return getEntities(resultSet);
	}
	
	public List<Entity> getEntities(ResultSet resultSet) {
		List<Entity> alTables = new ArrayList<Entity>();
		Entity currentEntite = null;
		ArrayList<Attribute> alAttributs = null;
		String checkdenom = "";
		String tablename = null;
		String name = null;
		String type = null;
		Integer size = null;
		String tableSchem = null;
		String tablePrefix = null;
		String pkColumnName = null;
		String fkColumnName = null;
		//int i = 0;
		ArrayList<String> pkColumnNames = new ArrayList<String>();
		ArrayList<String> fkColumnNames = new ArrayList<String>();
		try {
			while (resultSet.next()) {
				System.out.println(resultSet.toString());
				/*tableSchem = resultSet.getString("TABLE_SCHEM");
				if (!"sys".equalsIgnoreCase(tableSchem) && !"information_schema".equalsIgnoreCase(tableSchem)
						&& !"dbo".equalsIgnoreCase(tableSchem)) {
					tablename = resultSet.getString("TABLE_NAME");
					tablePrefix = resultSet.getString("TABLE_SCHEM");
					//System.out.println("TableName  : " + tablename + "\nTablePrefix: "+ tablePrefix);
					name = resultSet.getString("COLUMN_NAME");
					type = resultSet.getString("TYPE_NAME");
					size = resultSet.getInt("COLUMN_SIZE");
					if (!checkdenom.equals(tablename)) { // runs total number of table times
						// retrieve PKs
						pkColumnNames = new ArrayList<String>();
						ResultSet rs = metadata.getPrimaryKeys(dbName, tablePrefix, tablename);
						while (rs.next()) {
							pkColumnName = rs.getString("COLUMN_NAME");
							pkColumnNames.add(pkColumnName);
						}
						// Retrive FK
						fkColumnNames = new ArrayList<String>();
						ResultSet rsFk = metadata.getImportedKeys(dbName, tablePrefix, tablename);
						while (rsFk.next()) {
							fkColumnName = rsFk.getString("FKCOLUMN_NAME");
							fkColumnNames.add(fkColumnName);
						}
						currentEntite = new Entity(dbName, tablePrefix, tablename);
						if (tablePrefix.equalsIgnoreCase("Codes")) {
							currentEntite.setCodeTable(true);
							// Table Locale_Cd does not refer to
							// Codes.Locale_Locales
							if (!"Locale_Cd".equalsIgnoreCase(tablename)) {
								currentEntite.setConstantes(
										DbHelper.retrieveCdForConstantes(connection, dbName, tablePrefix, tablename));
							}
						}
						alAttributs = new ArrayList<Attribute>();
						Attribute currentAttribut = new Attribute(name, type, size);
						for (String pkCol : pkColumnNames) {
						//	System.out.println("pkCol " + pkCol);
						//	System.out.println(currentAttribut.getName());
							if (pkCol.equalsIgnoreCase(currentAttribut.getRawName())) {
								currentAttribut.setPrimaryKey(true);
								if (DbHelper.columnIsAutoincrement(connection, dbName, tablePrefix, tablename, pkCol)) {
									currentAttribut.setAutoincrement(true);
								}
							}
						}
						for (String fkCol : fkColumnNames) {
							//	System.out.println("pkCol " + pkCol);
							//	System.out.println(currentAttribut.getName());
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
							/*
							 * System.out.println("pkCol "+pkCol);
							 * System.out.println(currentAttribut.getName());
							 */
						/*	if (pkCol.equalsIgnoreCase(currentAttribut.getRawName())) {
								currentAttribut.setPrimaryKey(true);
								if (DbHelper.columnIsAutoincrement(connection, dbName, tablePrefix, tablename, pkCol)) {
									currentAttribut.setAutoincrement(true);
								}
							}
						}
						for (String fkCol : fkColumnNames) {
							if (fkCol.equalsIgnoreCase(currentAttribut.getRawName())) {
								//System.out.println("fkCol "+fkCol);
								//System.out.println(currentAttribut.getName());
								currentAttribut.setForeignKey(true);
							}
						}
						alAttributs.add(currentAttribut);
						currentEntite.setAlAttr(alAttributs);
					}
				}*/
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alTables;
	}

	
}
