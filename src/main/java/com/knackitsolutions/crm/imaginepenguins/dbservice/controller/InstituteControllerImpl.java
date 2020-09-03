package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.InstituteModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.InstituteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/institutes")
public class InstituteControllerImpl {

	private static final Logger log = LoggerFactory.getLogger(InstituteControllerImpl.class);
	
	@Autowired
	InstituteRepository instituteRepository;

	@Autowired
	InstituteModelAssembler instituteModelAssembler;

	@GetMapping("/{id}")
	public EntityModel<Institute> one(@PathVariable Integer id) {
		Optional<Institute> institute = instituteRepository.findById(id);
		return instituteModelAssembler.toModel(institute.
				orElseThrow(() -> new InstituteNotFoundException(id)));
	}
	
	@GetMapping
	public CollectionModel<EntityModel<Institute>> all() {
		List<EntityModel<Institute>> entityModelList = instituteRepository.findAll()
				.stream()
				.map(instituteModelAssembler::toModel)
				.collect(Collectors.toList());
		return  CollectionModel.of(entityModelList,
				linkTo(methodOn(InstituteControllerImpl.class).all()).withSelfRel());
	}

	@GetMapping("/{id}/branches")
	public ResponseEntity<List<Institute>> allBranches(@PathVariable("id") Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@PostMapping
	public ResponseEntity<?> newInstitute(Institute institute) {
		log.info("instituteType: {}", institute.getInstituteType());
		EntityModel<Institute> entityModel = instituteModelAssembler.toModel(
				instituteRepository.save(institute)
		);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
					.body(entityModel);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> replaceInstitute(Institute newInstitute, @PathVariable("id") Integer id) {
		Institute replacedInstitute = instituteRepository.findById(id)
				.map(institute -> {
					institute.setInstituteType(newInstitute.getInstituteType());
					institute.setName(newInstitute.getName());
					return instituteRepository.save(institute);
				}).orElseThrow(() -> new InstituteNotFoundException(id));
		EntityModel<Institute> model = instituteModelAssembler.toModel(replacedInstitute);
		return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(model);
	}
}
