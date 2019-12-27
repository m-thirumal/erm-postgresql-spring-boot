/**
 * 
 */
package in.thirumal.persistence;

/**
 * @author thirumal
 *
 */
public interface GenericDao<T> {

	T list(String schema);
}
