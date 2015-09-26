package se.majp.cms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import se.majp.cms.exception.BadRequestException;
import se.majp.cms.exception.EntityNotFoundException;
import se.majp.cms.model.Issue;
import se.majp.cms.model.Project;
import se.majp.cms.model.Status;
import se.majp.cms.model.Story;
import se.majp.cms.model.User;
import se.majp.cms.repository.IssueRepository;
import se.majp.cms.repository.ProjectRepository;
import se.majp.cms.repository.StoryRepository;
import se.majp.cms.repository.UserRepository;
import se.majp.cms.util.IdGenerator;

public class StoryServiceImpl implements StoryService
{
	@Autowired
	private StoryRepository storyRepository;

	@Autowired
	private IssueRepository issueRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	private final IdGenerator idGenerator = IdGenerator.getBuilder().length(8).characters('0', 'z').build();

	@Override
	public Story addStoryToBacklog(String projectId, Story story)
	{
		Project project = projectRepository.findByProjectId(projectId);

		if (project == null)
		{
			throw new EntityNotFoundException("No project found with that id");
		}

		story = new Story(idGenerator.getNextId(), story.getName(), story.getDescription(), project, story.getStatus(), story.getPriority());
		return storyRepository.save(story);
	}

	@Override
	public Story addStoryToUser(String userId, Story story)
	{
		User user = userRepository.findByUserId(userId);
		story = storyRepository.findByStoryId(story.getStoryId());

		if (user == null)
		{
			throw new EntityNotFoundException("No user found with that id");
		}

		story.setUser(user);
		return storyRepository.save(story);
	}

	@Override
	public Story addIssue(String storyId, Issue issue)
	{
		Story story = storyRepository.findByStoryId(storyId);

		if (story == null)
		{
			throw new EntityNotFoundException("No story found with that storyId");
		}

		issue = new Issue(idGenerator.getNextId(), issue.getTitle(), issue.getDescription(), story);
		issueRepository.save(issue);

		return storyRepository.findByStoryId(storyId);
	}

	@Override
	public Story changeStatus(String storyId, String stringStatus)
	{
		Story story = storyRepository.findByStoryId(storyId);
		Status status = null;

		if (story == null)
		{
			throw new EntityNotFoundException("No story found with that storyId");
		}

		if (isValidStatus(stringStatus))
		{
			status = Status.valueOf(stringStatus);
		}
		else
		{
			throw new BadRequestException("Not a valid status");
		}

		switch (story.getStatus())
		{
		case PENDING:
			switch (status)
			{
			case INPROGRESS:
				story.changeStatus(status);
				break;
			default:
				throw new IllegalArgumentException("status can only be changed to INPROGRESS");
			}
			break;

		case ISSUED:
			switch (status)
			{
			case TEST:
				story.setUser(null);
				story.changeStatus(status);
				break;
			default:
				throw new IllegalArgumentException("Status can only be changed to TEST");
			}
			break;

		case INPROGRESS:
			switch (status)
			{
			case TEST:
				story.setUser(null);
				story.changeStatus(status);
				break;
			default:
				throw new IllegalArgumentException("Status can only be changed to TEST");
			}
			break;

		case TEST:
			switch (status)
			{
			case DONE:
				story.setUser(null);
				story.changeStatus(status);
				break;
			case ISSUED:
				story.setUser(null);
				story.changeStatus(status);
				break;
			default:
				throw new IllegalArgumentException("Status can only be changed to TEST or ISSUED");
			}
			break;

		case DONE:
			switch (status)
			{
			case ISSUED:
				story.changeStatus(status);
				break;
			default:
				throw new IllegalArgumentException("Status can only be changed to ISSUED");
			}
			break;

		default:
			throw new IllegalArgumentException("Not a valid status");
		}

		return storyRepository.save(story);
	}

	@Override
	public List<Story> findAllStoriesWithIssues()
	{
		return storyRepository.findStoriesWithIssues();
	}

	@Override
	public List<Story> findByDescriptionContaining(String description)
	{
		List<Story> stories = storyRepository.findByDescriptionContaining(description);
		if (stories.isEmpty())
		{
			throw new EntityNotFoundException("No stories matching description");
		}

		return stories;
	}

	@Override
	public List<Story> findBacklogForProject(String projectId)
	{
		if (projectRepository.findByProjectId(projectId) != null)
		{
			return storyRepository.findBacklogForProject(projectId);
		}

		throw new EntityNotFoundException("Project not in DB");
	}

	@Override
	public List<Story> findAllStoriesInProject(String projectId)
	{
		if (projectRepository.findByProjectId(projectId) != null)
		{
			return storyRepository.findByProject(projectId);
		}

		throw new EntityNotFoundException("Project not in DB");
	}

	@Override
	public List<Story> findAllStoriesByStatus(String status)
	{
		if (isValidStatus(status))
		{
			return storyRepository.findByStatus(Status.valueOf(status));
		}

		throw new BadRequestException("Not a valid status");
	}

	@Override
	public List<Story> findAllStoriesAssignedToUser(String userId)
	{
		if (userRepository.findByUserId(userId) != null)
		{
			List<Story> stories = storyRepository.findByUser(userId);
			return stories;
		}

		throw new EntityNotFoundException("user not found");
	}

	@Override
	public List<Story> findAllStoriesByUserAndProject(String projectId, String userId)
	{
		if (userRepository.findByUserId(userId) != null)
		{
			List<Story> stories = storyRepository.findByUserAndProject(userId, projectId);

			return stories;
		}

		throw new EntityNotFoundException("user not found");
	}

	@Override
	public void removeStory(String storyId)
	{
		Story story = storyRepository.findByStoryId(storyId);

		if (story == null)
		{
			throw new EntityNotFoundException("No story found with that storyId");
		}

		storyRepository.delete(story);
	}

	@Override
	public List<Story> findAllStoriesByStatusAndCreatedBetweenTwoDates(String from, String to, String status)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date dateFrom = null;
		Date dateTo = null;
		try
		{
			dateFrom = formatter.parse(from);
			dateTo = formatter.parse(to);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		List<Story> stories = storyRepository.findByStatusAndCreatedBetweenTwoDates(dateFrom, dateTo, Status.valueOf(status));
		if (stories.isEmpty())
		{
			throw new EntityNotFoundException("No stories found between " + from + " and " + to + " with status " + status);
		}

		return stories;
	}

	@Override
	public List<Story> findAllStories(Pageable page)
	{
		List<Story> stories = new ArrayList<>();
		stories.addAll(storyRepository.findAll(page).getContent());
		return stories;
	}

	private boolean isValidStatus(String stringStatus)
	{
		for (Status status : Status.values())
		{
			if (status.toString().equals(stringStatus))
			{
				return true;
			}
		}

		return false;
	}
}
