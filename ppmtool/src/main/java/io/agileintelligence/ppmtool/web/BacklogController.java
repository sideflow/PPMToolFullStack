package io.agileintelligence.ppmtool.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.services.MapValidationErrorService;
import io.agileintelligence.ppmtool.services.ProjectTaskService;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

	
	@Autowired
	private ProjectTaskService projectTaskService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("/{backlogId}")
	public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlogId) {
		
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
		if (errorMap != null) {
			 return errorMap;
		}
		
		ProjectTask projectTaskCreated = projectTaskService.addProjectTask(backlogId, projectTask);
		return new ResponseEntity<ProjectTask> (projectTaskCreated, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{backlogId}")
	public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String backlogId) {
		
		return new ResponseEntity<List<ProjectTask>>(projectTaskService.findBacklogBy(backlogId), HttpStatus.OK);
	}
	
	
	@GetMapping("/{backlogId}/{projectTaskId}")
	public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String projectTaskId) {
		ProjectTask projectTask = projectTaskService.findByProjectSequence(backlogId, projectTaskId);
		return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
	}
	
	
	@PatchMapping("/{backlogId}/{projectTaskId}")
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlogId, @PathVariable String projectTaskId) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
		if (errorMap != null) {
			 return errorMap;
		}
		
		ProjectTask updatedTask = projectTaskService.updateProjectTaskSequence(projectTask, backlogId);
	
		return new ResponseEntity<ProjectTask> (updatedTask, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{backlogId}/{projectTaskId}")
	public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String projectTaskId) {
		projectTaskService.deleteProjectTask(backlogId, projectTaskId);
		return new ResponseEntity<String> ("Porject task '"+projectTaskId + "' was deleted successfully" , HttpStatus.OK);
	}
}
