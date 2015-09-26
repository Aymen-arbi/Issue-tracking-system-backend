package se.majp.cms.service.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import se.majp.cms.exception.EntityNotFoundException;
import se.majp.cms.model.Project;
import se.majp.cms.model.Role;
import se.majp.cms.model.TeamMember;
import se.majp.cms.model.User;
import se.majp.cms.service.ProjectService;
import se.majp.cms.test.config.IntegrationTestBaseClass;

public class ProjectServiceIntegrationTest extends IntegrationTestBaseClass
{
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Autowired
	private ProjectService projectService;

	private Project projectToSave = new Project(PROJECT_NAME, PROJECT_DESCRIPTION);

	@Test
	public void addOrUpdateProject_shouldReturnProjectWithId()
	{
		Project project = projectService.addOrUpdateProject(projectToSave);
		assertNotNull(project.getId());
	}

	@Test
	public void addOrUpdateTeamMember_shouldThrowEntityNotFoundException()
	{
		exception.expect(EntityNotFoundException.class);
		exception.expectMessage("No project found");

		User user = userRepository.findByUserId(USER_USERID);

		projectService.addOrUpdateTeamMember("NO MATCH", new TeamMember(user, Role.OWNER));
	}

	@Test
	public void addOrUpdateTeamMember_shouldReturnProjectWithUser()
	{
		User user = userRepository.findByUserId(USER_USERID);
		Project project = projectService.addOrUpdateTeamMember(PROJECT_PROJECTID, new TeamMember(user, Role.OWNER));

		assertThat(project.getTeam().getUsers().size(), is(1));
	}

	@Test
	public void removeTeamMember_shouldThrowEntityNotFoundException()
	{
		exception.expect(EntityNotFoundException.class);
		exception.expectMessage("not found");

		projectService.removeTeamMember("NO MATCH", "NO MATCH");
	}

	@Test
	public void removeTeamMember_shouldReturnProjectWithNoUsers()
	{
		Project project = projectService.findByProjectId(PROJECT_PROJECTID);

		assertThat(project.getTeam().getUsers().size(), is(1));

		project = projectService.removeTeamMember(project.getProjectId(), USER_USERID);

		assertThat(project.getTeam().getUsers().size(), is(0));
	}
}
