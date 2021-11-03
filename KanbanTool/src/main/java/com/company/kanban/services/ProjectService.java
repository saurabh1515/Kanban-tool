package com.company.kanban.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.kanban.exceptions.ProjectIdException;
import com.company.kanban.exceptions.ProjectNotFoundException;
import com.company.kanban.model.Backlog;
import com.company.kanban.model.Project;
import com.company.kanban.model.User;
import com.company.kanban.repositories.BacklogRepository;
import com.company.kanban.repositories.ProjectRepository;
import com.company.kanban.repositories.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdateProject(Project project, String username) {
		
		if(project.getId() != null) {
			Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
			
			if(existingProject != null &&(!existingProject.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundException("Project not found in your account");
			}else if(existingProject == null) {
				throw new ProjectNotFoundException("Project with ID:'"+project.getProjectIdentifier()+"' cannot be updated because it does not exist!");
			}
		} 
		
		try {
			
			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			
			
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			//saving new project
			if(project.getId()==null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			
			//updating project
			if(project.getId()!=null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			
			return projectRepository.save(project);
			
		}catch(Exception e) {
			throw new ProjectIdException("Project ID '"+ project.getProjectIdentifier().toUpperCase()+"' already exists.");
		}
	
	}
	
	//find by ID
	public Project findProjectByIdentifier(String projectId, String username) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Project ID '"+ projectId+"' does not exist.");
		}
		
		if(!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account!");
		}
		
		
		return project;
	}
	
	//find All
	public Iterable<Project> findAllProjects(String username){
		return projectRepository.findAllByProjectLeader(username);
	}
	
	//delete by ID
	public void deleteProjectByIdentifier(String projectId, String username) {

		
		
		projectRepository.delete(findProjectByIdentifier(projectId, username));
		
	}
	

}
