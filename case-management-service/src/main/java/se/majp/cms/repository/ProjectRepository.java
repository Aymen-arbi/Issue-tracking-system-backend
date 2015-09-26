package se.majp.cms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import se.majp.cms.model.Project;
import se.majp.cms.model.User;

public interface ProjectRepository extends CrudRepository<Project, Long>
{
	@Query("select p from Project p where ?1 in indices(p.team.users)")
	List<Project> findAllProjectsForUser(User user);

	@Query("select p from Project p where p.projectId = ?1")
	Project findByProjectId(String projectId);

	Page<Project> findAll(Pageable page);
}
