package se.majp.cms.main;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;

import se.majp.cms.model.Issue;
import se.majp.cms.model.Priority;
import se.majp.cms.model.Project;
import se.majp.cms.model.Role;
import se.majp.cms.model.Status;
import se.majp.cms.model.Story;
import se.majp.cms.model.TeamMember;
import se.majp.cms.model.User;
import se.majp.cms.service.IssueService;
import se.majp.cms.service.ProjectService;
import se.majp.cms.service.StoryService;
import se.majp.cms.service.UserService;
import se.majp.cms.util.IdGenerator;

public class Main
{
	public static void main(String[] args)
	{
		IdGenerator generator = IdGenerator.getBuilder().characters('0', 'z').length(8).build();
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext())
		{
			context.scan("se.majp.cms");
			context.refresh();
			UserService userService = context.getBean(UserService.class);
			StoryService storyService = context.getBean(StoryService.class);
			ProjectService projectService = context.getBean(ProjectService.class);
			IssueService issueService = context.getBean(IssueService.class);
			User user = new User("BoAhl@example.com", "Bo", "Ahl", "BoThaMaster");
			Project project = new Project("Get shit done!", "Lets do this shit");
			Project project2 = new Project("Get more shit done", "Lets never do this");
			Story story = new Story(generator.getNextId(), "Repository", "Do shit", project, Status.PENDING, Priority.HIGH);
			Story story2 = new Story(generator.getNextId(), "Service", "Do shit", project2, Status.PENDING, Priority.LOW);
			story.setUser(user);
			Issue issue = new Issue(generator.getNextId(), "Issue Ttitle", "You screwed up you idiot", story);
			// userService.addOrUpdateUser(user);
			// projectService.addOrUpdateProject(project);
			// projectService.addOrUpdateProject(project2);
			user = userService.findByFirstNameOrLastNameOrEmail("Ahl").get(0);
			project = projectService.findByProjectId("1fMALtfv");
			// storyService.addStoryToBacklog(project.getProjectId(), story);
			// storyService.addStoryToBacklog(project.getProjectId(), story2);
			List<Story> storires = storyService.findAllStoriesInProject("1fMALtfv");
			storyService.addStoryToUser(user.getUserId(), storires.get(0));
			storyService.addStoryToUser(user.getUserId(), storires.get(1));
			// issueService.save(issue);
			TeamMember member = new TeamMember(user, Role.OWNER);
			projectService.addOrUpdateTeamMember("1fMALtfv", member);

			// System.out.println("Find story by Project \n-------------------------");
			// storyService.findBacklogForProject(project.getProjectId()).forEach(System.out::println);
			// System.out.println("Find story by User \n-------------------------");
			// storyService.findByUser(user.getUserId()).forEach(System.out::println);
			// System.out.println("Find projects by User \n-------------------------");
			// projectService.findAllProjectsByUser(user.getUserId()).forEach(System.out::println);
			// System.out.println("Find story with Issue \n-------------------------");
			// storyService.findAllStoriesWithIssues().forEach(System.out::println);
			// System.out.println("Find story by Description \n-------------------------");
			// storyService.findByDescriptionContaining("shit").forEach(System.out::println);
			// System.out.println("-----------------");
			// userService.findByProject(project.getProjectId()).forEach(System.out::println);

			System.out.println("*********************************");

			storyService.findAllStoriesByStatusAndCreatedBetweenTwoDates("2015-09-16 14:00",
					"2015-09-16 17:30", "PENDING").forEach(System.out::println);
			System.out.println("*********************************");
			storyService.findAllStories(new PageRequest(0, 3)).forEach(System.out::println);
		}
	}

}