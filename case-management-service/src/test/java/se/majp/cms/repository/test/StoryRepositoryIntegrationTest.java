package se.majp.cms.repository.test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import se.majp.cms.model.Issue;
import se.majp.cms.model.Project;
import se.majp.cms.model.Story;
import se.majp.cms.test.config.IntegrationTestBaseClass;

public class StoryRepositoryIntegrationTest extends IntegrationTestBaseClass
{
	@Test
	public void findByProject_NoMatch_shouldReturnEmptyList()
	{
		List<Story> stories = storyRepository.findByProject("NO MATCH");
		assertThat(stories.size(), is(0));
	}

	@Test
	public void findByProject_Match_shouldReturnListOfSizeOne()
	{
		List<Story> stories = storyRepository.findByProject(PROJECT_PROJECTID);
		checkSizeOneAndStoryProperties(stories);
	}

	@Test
	public void findByUser_NoMatch_shouldReturnEmptyList()
	{
		List<Story> stories = storyRepository.findByUser("NO MATCH");
		assertThat(stories.size(), is(0));
	}

	@Test
	public void findByUser_Match_shouldReturnListOfSizeOne()
	{
		List<Story> stories = storyRepository.findByUser(USER_USERID);
		checkSizeOneAndStoryProperties(stories);
	}

	@Test
	public void findByUserAndProject_NoMatch_shouldReturnEmptyList()
	{
		List<Story> stories = storyRepository.findByUserAndProject("NO MATCH", "NO MATCH");
		assertThat(stories.size(), is(0));
	}

	@Test
	public void findByUserAndProject_Match_shouldReturnListOfSizeOne()
	{
		List<Story> stories = storyRepository.findByUserAndProject(USER_USERID, PROJECT_PROJECTID);
		checkSizeOneAndStoryProperties(stories);
	}

	@Test
	public void findStoriesWithIssues_Match_shouldReturnListOfSizeOne()
	{
		Story story = storyRepository.findByStoryId(STORY_STORYID);
		Issue issue = new Issue("issueId", "name", "description", story);
		issueRepository.save(issue);

		List<Story> stories = storyRepository.findStoriesWithIssues();
		checkSizeOneAndStoryProperties(stories);
	}

	@Test
	public void findBacklogForProject_NoMatch_shouldReturnEmptyList()
	{
		List<Story> stories = storyRepository.findBacklogForProject("NO MATCH");
		assertThat(stories.size(), is(0));
	}

	@Test
	public void findBacklogForProject_Match_shouldReturnListOfSizeOne()
	{
		Story story = storyRepository.findByStoryId(STORY_STORYID);
		story.setUser(null);
		storyRepository.save(story);

		List<Story> stories = storyRepository.findBacklogForProject(PROJECT_PROJECTID);
		checkSizeOneAndStoryProperties(stories);
	}

	private void checkSizeOneAndStoryProperties(List<Story> stories)
	{
		Project project = projectRepository.findByProjectId(PROJECT_PROJECTID);

		assertThat(stories.size(), is(1));
		assertThat(stories.get(0), allOf(
				hasProperty("storyId", is(STORY_STORYID)),
				hasProperty("name", is(STORY_NAME)),
				hasProperty("description", is(STORY_DESCRIPTION)),
				hasProperty("project", is(project)),
				hasProperty("status", is(STORY_STATUS)),
				hasProperty("priority", is(STORY_PRIORITY))));
	}
}
