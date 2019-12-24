/**
 * 
 */
package in.thirumal.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.thirumal.config.TargetConfig;

/**
 * @author Thirumal
 *
 */
@Service
public class GeneratorService {

	@Autowired
	TargetConfig targetConfig;
	
	public boolean list() {
		System.out.println(targetConfig.toString());
		return true;
	}
}
