package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.InstituteModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance.AttendanceController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.InstituteMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDepartmentDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.InstituteNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.InstituteService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.document.AmazonDocumentStorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/institutes")
@Slf4j
@RequiredArgsConstructor
public class InstituteController {

	private final IAuthenticationFacade authenticationFacade;
	private final InstituteRepository instituteRepository;
	private final InstituteModelAssembler instituteModelAssembler;
	private final UserService userService;
	private final InstituteMapper instituteMapper;
	private final InstituteDepartmentRepository instituteDepartmentRepository;
	private final AmazonDocumentStorageClient storageClient;
	private final InstituteService instituteService;

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
				linkTo(methodOn(InstituteController.class).all()).withSelfRel());
	}

	@GetMapping("/departments")
	public CollectionModel<EntityModel<InstituteDepartmentDTO>> loadDepartments(){
		UserContext userContext = (UserContext) authenticationFacade.getAuthentication();
		User user = userService
				.findById(userContext.getUserId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userContext.getUserId()));
		Employee employee = null;
		if (!(user instanceof Employee)) {
			throw new RuntimeException("UserDTO is not an Employee of any institute.");
		}
		employee = (Employee) user;
		List<EntityModel<InstituteDepartmentDTO>> dtos = instituteMapper.entityToDTO(instituteDepartmentRepository
				.findByInstituteId(employee.getInstitute().getId()))
				.stream()
				.map(EntityModel::of)
				.map(em -> em.add(
						linkTo(methodOn(AttendanceController.class)
								.loadUsers(null, null, 0, 10)).withRel("load-employees")
				))
				.collect(Collectors.toList());

		return CollectionModel.of(dtos
				, linkTo(methodOn(InstituteController.class).loadDepartments()).withSelfRel()
				, linkTo(methodOn(TeacherController.class).classes()).withRel("load-classes"));
	}


	@PostMapping
	public ResponseEntity<EntityModel<String>> newInstitute(@RequestBody InstituteDTO dto) throws URISyntaxException {
		log.info("Institute type: {}", dto.getInstituteType());
		Institute newInstitute = instituteRepository.save(instituteMapper.dtoToEntity(dto));

		EntityModel<Institute> entityModel = instituteModelAssembler.toModel(newInstitute);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
					.body(EntityModel.of("success"
							, linkTo(
									methodOn(getClass())
											.uploadDoc(null, newInstitute.getId(), null)
							).withRel("upload-doc")
							, linkTo(
									methodOn(getClass()).downloadDoc(newInstitute.getId(), null, null)
							).withRel("download-doc")
						)
					);
	}

	@PutMapping("/{instituteId}")
	public ResponseEntity<?> replaceInstitute(Institute newInstitute, @PathVariable("instituteId") Integer id) {
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

	@PutMapping("/{instituteId}/upload")
	public ResponseEntity<String> uploadDoc(@RequestParam("file") MultipartFile multipartFile
			, @PathVariable Integer instituteId, @RequestParam("doc-type") InstituteDocumentType instituteDocumentType) throws URISyntaxException {
		String fileName = storageClient.storeFile(multipartFile
				, instituteService
						.findById(instituteId)
						.orElseThrow(() -> new InstituteNotFoundException(instituteId))
				, instituteDocumentType);
		return ResponseEntity.created(new URI(fileName)).body("success!!");
	}

	@GetMapping("/{instituteId}/download")
	public ResponseEntity<Resource> downloadDoc(@PathVariable Integer instituteId
			, @RequestParam("doc-type") InstituteDocumentType instituteDocumentType
			, HttpServletRequest request) {
		String fileName = storageClient.getDocumentName(instituteId, instituteDocumentType);
		Resource resource = null;
		if (fileName == null || fileName.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		try {
			resource = storageClient.loadFileAsResource(fileName);
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);

		}
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ioException) {
			log.error("Could not determine the file type.");
			log.error("IGNORE. " + ioException.getMessage(), ioException);
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity
				.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filepath=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
