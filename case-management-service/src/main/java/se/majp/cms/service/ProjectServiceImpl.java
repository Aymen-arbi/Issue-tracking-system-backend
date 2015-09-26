package se.majp.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import se.majp.cms.exception.EntityNotFoundException;
import se.majp.cms.model.Project;
import se.majp.cms.model.TeamMember;
import se.majp.cms.model.User;
import se.majp.cms.repository.ProjectRepository;
import se.majp.cms.repository.UserRepository;
import se.majp.cms.util.IdGenerator;

public class ProjectServiceImpl implements ProjectService
{
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	private final IdGenerator idGenerator = IdGenerator.getBuilder().length(8).characters('0', 'z').build();

	@Override
	public Project addOrUpdateProject(Project project)
	{
		if (project.getProjectId() == null)
		{
			project = new Project(idGenerator.getNextId(), project.getName(), project.getDescription());

			return projectRepository.save(project);
		}

		Project projectFromDb = projectRepository.findByProjectId(project.getProjectId());

		if (projectFromDb == null)
		{
			throw new EntityNotFoundException("No project with that projectId");
		}

		projectFromDb.setName(project.getName());
		projectFromDb.setDescription(project.getDescription());

		return projectRepository.save(projectFromDb);
	}

	@Override
	public Project findByProjectId(String projectId)
	{
		Project project = projectRepository.findByProjectId(projectId);

		if (project == null)
		{
			throw new EntityNotFoundException("Project not found in DB");
		}

		return project;
	}

	@Override
	public List<Project> findAllProjects()
	{
		List<Project> projects = new ArrayList<>();
		projectRepository.findAll().forEach(project -> {
			projects.add(project);
		});

		return projects;
	}

	@Override
	public List<Project> findAllProjects(Pageable page)
	{
		List<Project> projects = new ArrayList<>();
		projects.addAll(projectRepository.findAll(page).getContent());
		return projects;
	}

	@Override
	public List<Project> findAllProjectsByUser(String userId)
	{
		User user = userRepository.findByUserId(userId);

		if (user == null)
		{
			throw new EntityNotFoundException("User not found with a userId: " + userId);
		}

		return projectRepository.findAllProjectsForUser(user);
	}

	@Override
	public Project addOrUpdateTeamMember(String projectId, TeamMember teamMember)
	{
		Project project = projectRepository.findByProjectId(projectId);
		User user = userRepository.findByUserId(teamMember.getUser().getUserId());

		if (project == null)
		{
			throw new EntityNotFoundException("No project found with projectId: " + projectId);
		}

		project.getTeam().addUser(user, teamMember.getRole());
		return projectRepository.save(project);
	}

	@Override
	public Project removeTeamMember(String projectId, String userId)
	{
		Project project = projectRepository.findByProjectId(projectId);
		User user = userRepository.findByUserId(userId);

		if (project == null || user == null)
		{
			throw new EntityNotFoundException("Project or user not found");
		}

		project.getTeam().removeUser(user);
		return projectRepository.save(project);
	}

	@Override
	public void removeProject(String projectId)
	{
		Project project = projectRepository.findByProjectId(projectId);
		projectRepository.delete(project);
	}
}
