/**
 * 
 */
package in.thirumal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("")
	public boolean generatedao(@RequestParam(value = "databaseName", defaultValue = "icms") String databaseName, 
			@RequestParam(value = "schemaName", defaultValue = "indsolv") String schemaName) {
		return genericService.generate(databaseName, schemaName);
	}
	
}
