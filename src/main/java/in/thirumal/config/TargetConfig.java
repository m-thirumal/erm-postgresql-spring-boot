/**
 * 
 */
package in.thirumal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Thirumal
 *
 */
@Component
@ConfigurationProperties(prefix="in.thirumal")
@Getter@Setter
@ToString
public class TargetConfig {

	private String packageDao;
	private String packageModel;
	private String rootFolder;
	private String modelFolder;
	private String daoFolder;
}
