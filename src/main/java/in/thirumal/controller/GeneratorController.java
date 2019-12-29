/**
 * 
 */
package in.thirumal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.thirumal.rest.GenericService;

/**
 * @author Thirumal
 *
 */
@RestController
@RequestMapping("/generate")
public class GeneratorController {

	@Autowired
	GenericService genericService;
	
	@GetMapping("/{databaseName}/{schemaName}")
	public boolean generatedao(@PathVariable("databaseName") String databaseName, 
			@PathVariable("schemaName") String schemaName) {
		return genericService.generate(databaseName, schemaName);
	}
	
}
