package se.majp.cms.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import se.majp.cms.model.Issue;
import se.majp.cms.model.Story;

public interface StoryService
{
	Story addStoryToBacklog(String projectId, Story story);

	Story addStoryToUser(String userId, Story story);

	Story addIssue(String storyId, Issue issue);

	Story changeStatus(String storyId, String status);

	List<Story> findAllStoriesWithIssues();

	List<Story> findByDescriptionContaining(String description);

	List<Story> findBacklogForProject(String projectId);

	List<Story> findAllStoriesInProject(String projectId);

	List<Story> findAllStoriesByStatus(String status);

	List<Story> findAllStoriesAssignedToUser(String userId);

	List<Story> findAllStoriesByUserAndProject(String userId, String projectId);

	List<Story> findAllStoriesByStatusAndCreatedBetweenTwoDates(String from, String to, String status);

	List<Story> findAllStories(Pageable page);

	void removeStory(String storyId);
}
