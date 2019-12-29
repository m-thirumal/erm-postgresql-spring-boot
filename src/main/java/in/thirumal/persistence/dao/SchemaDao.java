/**
 * 
 */
package in.thirumal.persistence.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
	public Entity list(String databaseName, String schemaName) {
		this.jdbcTemplate.execute(new ConnectionCallback<ResultSet>() {
	        @Override
	        public ResultSet doInConnection(Connection connection) throws SQLException {

	            return connection.getMetaData().getColumns(databaseName, schemaName, null, null);
	        }
	    });
		return null;
	}
	
	
}
