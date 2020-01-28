package io.agileintelligence.ppmtool.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.ProjectException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
	private ProjectRepository projectRepository;
    
    @Autowired
    private BacklogRepository backlogRepository;
    
    @Autowired
    private UserRepository userRepository;

	public Project saveOrUpdateProject(Project project, String username) {
		

		if(project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            
            if(existingProject !=null &&(!existingProject.getProjectLeader().equals(username))){
            
            	throw new ProjectException("Project not found in your account");
            
            } else if(existingProject == null){
                
            	throw new ProjectException("Project with ID: '"+project.getProjectIdentifier()+"' cannot be updated because it doesn't exist");
            }
        }
		
		String projectIdentifier = project.getProjectIdentifier().toUpperCase();
		try {
			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			
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
	
	public Project findProjectByIdentifier(String projectIdentifier, String username) {
		String projectId = projectIdentifier.toUpperCase();
		Project project = projectRepository.findByProjectIdentifier(projectId);
		if (project == null) {
			throw new ProjectException("Project ID '" + projectId  +"' does not exist");
		}
		if (! project.getProjectLeader().equals(username))  {
			throw new ProjectException("Project not found exception.");
		}
		return project;
	}

	public Iterable<Project> findAllProjects(String username) {
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProject(String projectIdentifier, String username) {
//		String projectId = projectIdentifier.toUpperCase();
//		Project project = projectRepository.findByProjectIdentifier(projectId);
//		if (project == null) {
//			throw new ProjectException("Cannot delete project with ID "+projectId+". This project doesn't exist");
//		}
		projectRepository.delete(findProjectByIdentifier(projectIdentifier, username));
	}




}
