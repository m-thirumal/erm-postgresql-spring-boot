/**
 * 
 */
package in.thirumal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author thirumal
 *
 */
@Configuration
@PropertySource("classpath:target.yml")
public class TargetConfig {

}
