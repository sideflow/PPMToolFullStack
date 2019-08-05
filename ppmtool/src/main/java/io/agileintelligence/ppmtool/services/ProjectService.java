package io.agileintelligence.ppmtool.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
	private ProjectRepository projectRepository;
    
    @Autowired
    private BacklogRepository backlogRepository;

	public Project saveOrUpdateProject(Project project) {
		String projectIdentifier = project.getProjectIdentifier().toUpperCase();
		try {
			project.setProjectIdentifier(projectIdentifier);
			handleNewOrExistingBackllog(project, projectIdentifier);
			
			return projectRepository.save(project);
		} catch (Exception e) {
			LOGGER.warn("!!! Exception at save or update : {}.", e.toString());
			throw new ProjectException("Save or update Project: '"+ projectIdentifier +"' failed "+e.getMessage());
		}
	}



	private void handleNewOrExistingBackllog(Project project, String projectIdentifier) {
		createNewBacklog(project, projectIdentifier); 			
		attachExistingBacklog(project, projectIdentifier);
	}	
	
	

	private void createNewBacklog(Project project, String projectIdentifier) {
		if (project.getId() == null) {
			Backlog backlog = new Backlog();
			backlog.setProjectIdentifier(projectIdentifier);
			project.setBacklog(backlog);
		}
	}

	private void attachExistingBacklog(Project project, String projectIdentifier) {
		String projectId = projectIdentifier.toUpperCase();
		if (project.getId() != null) {
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectId);
			if (project.getBacklog() == null) {
				project.setBacklog(backlog);
			}
		}
	}
	
	public Project findProjectByIdentifier(String projectIdentifier) {
		String projectId = projectIdentifier.toUpperCase();
		Project project = projectRepository.findByProjectIdentifier(projectId);
		if (project == null) {
			throw new ProjectException("Project ID '" + projectId  +"' does not exist");
		}
		return project;
	}

	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProject(String projectIdentifier) {
		String projectId = projectIdentifier.toUpperCase();
		Project project = projectRepository.findByProjectIdentifier(projectId);
		if (project == null) {
			throw new ProjectException("Cannot delete project with ID "+projectId+". This project doesn't exist");
		}
		projectRepository.delete(project);
	}




}
