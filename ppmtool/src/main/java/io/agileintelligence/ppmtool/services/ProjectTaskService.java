package io.agileintelligence.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
    @Autowired
	private ProjectRepository projectRepository;
		
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		String projectId = projectIdentifier.toUpperCase();
		Backlog backlog  = backlogRepository.findByProjectIdentifier(projectId);
		if (backlog == null) {
			throw new ProjectException("Project ID '" + projectId +"' does not exist");
		}
		projectTask.setBacklog(backlog);
		Integer backlogSequence = backlog.getPTSequence();
		backlogSequence++;
		backlog.setPTSequence(backlogSequence);
		projectTask.setProjectSequence(projectId+"-"+backlogSequence);
		projectTask.setProjectIdentifier(projectId);
		
		if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}
		
		if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
			projectTask.setStatus("TO_DO");
		}
		backlogRepository.save(backlog);
		return projectTaskRepository.save(projectTask); 
	}
	
	
	public List<ProjectTask> findBacklogBy(String projectIdentifier) {
		String projectId = projectIdentifier.toUpperCase();
		Project project = projectRepository.findByProjectIdentifier(projectId);
		if (project == null) {
			throw new ProjectException("Project ID '" + projectId +"' does not exist");
		}
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
	}
	
	public ProjectTask findByProjectSequence(String backlogId, String projectTaskId) {
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
		if (backlog == null) {
			throw new ProjectException("Project ID '" + backlogId +"' does not exist");
		}
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectTaskId);
		if (projectTask == null) {
			throw new ProjectException("projectTask '" + projectTaskId +"' does not exist");
		}
		if (! projectTask.getProjectIdentifier().equals(backlogId)) {
			throw new ProjectException("Project task '" + projectTaskId +"' does not exist in '"+backlogId+"'");
		}
		
		return projectTask;
	}
	
	public ProjectTask updateProjectTaskSequence(ProjectTask updatedTask, String backlogId) {
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
		if (backlog == null) {
			throw new ProjectException("Project ID '" + backlogId +"' does not exist");
		}
		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(updatedTask.getProjectSequence());
		if (projectTask == null) {
			throw new ProjectException("projectTask '" + updatedTask.getProjectSequence() +"' does not exist");
		}
		if (! projectTask.getProjectIdentifier().equals(backlogId)) {
			throw new ProjectException("Project task '" + updatedTask.getProjectSequence() +"' does not exist in '"+backlogId+"'");
		}
		updatedTask.setBacklog(backlog);
		projectTask = updatedTask;
		return projectTaskRepository.save(projectTask);
	}


	public void deleteProjectTask(String backlogId, String projectTaskId) {
		ProjectTask projectTask = findByProjectSequence(backlogId, projectTaskId);
		
		// Cascade issue
//		 Backlog backlog = projectTask.getBacklog();
//	     List<ProjectTask> pts = backlog.getProjectTasks();
//	     pts.remove(projectTask);
//	     backlogRepository.save(backlog);
	
		projectTaskRepository.delete(projectTask);
	}

}
