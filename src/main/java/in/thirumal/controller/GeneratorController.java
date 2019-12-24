/**
 * 
 */
package in.thirumal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.thirumal.rest.service.GeneratorService;

/**
 * @author Thirumal
 *
 */
@RestController
@RequestMapping("/generate")
public class GeneratorController {

	@Autowired
	GeneratorService generatorService;
	@GetMapping("")
	public boolean generatedao() {
		return generatorService.list();
	}
}
