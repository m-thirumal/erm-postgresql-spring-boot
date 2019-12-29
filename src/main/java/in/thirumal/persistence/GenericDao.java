/**
 * 
 */
package in.thirumal.persistence;

import java.util.List;

/**
 * @author thirumal
 *
 */
public interface GenericDao<T> {

	List<T> list(String databaseName, String schemaName);
}
