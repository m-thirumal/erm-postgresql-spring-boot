/**
 * 
 */
package in.thirumal.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Entity list(String schema) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
