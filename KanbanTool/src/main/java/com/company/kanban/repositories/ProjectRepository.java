package com.company.kanban.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.kanban.model.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	Project findByProjectIdentifier(String projectId);
	
	@Override
	Iterable<Project> findAll(); 
	
	Iterable<Project> findAllByProjectLeader(String username);

}
