package se.majp.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import se.majp.cms.service.IssueService;
import se.majp.cms.service.IssueServiceImpl;
import se.majp.cms.service.ProjectService;
import se.majp.cms.service.ProjectServiceImpl;
import se.majp.cms.service.StoryService;
import se.majp.cms.service.StoryServiceImpl;
import se.majp.cms.service.UserService;
import se.majp.cms.service.UserServiceImpl;

@Configuration
@EnableJpaRepositories("se.majp.cms.repository")
public class ServiceConfig
{
	@Bean
	public UserService userService()
	{
		return new UserServiceImpl();
	}

	@Bean
	public ProjectService projectService()
	{
		return new ProjectServiceImpl();
	}

	@Bean
	public StoryService storyService()
	{
		return new StoryServiceImpl();
	}

	@Bean
	public IssueService issueService()
	{
		return new IssueServiceImpl();
	}
}
