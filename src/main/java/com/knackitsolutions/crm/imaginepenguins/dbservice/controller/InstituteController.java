package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;

public interface InstituteController {
	
	@GetMapping("/institutes")
	CollectionModel<EntityModel<Institute>> all();
	
	@GetMapping("/institutes/{regId}")
	EntityModel<Institute> one(@PathVariable("regId") final Integer regId);
	
	@GetMapping("/institutes/{id}/branches")
	ResponseEntity<?> allBranches(@PathVariable("id") final Integer id);
	
	@PostMapping("/institutes")
	ResponseEntity<?> newInstitute(@RequestBody Institute institute);

	@PutMapping("/institutes/{id}")
	ResponseEntity<?> replaceInstitute(@RequestBody Institute institute, @PathVariable Integer id);
	//Get Director
	
	//update any info
	
	//create new institute
	
	//delete institute
}
