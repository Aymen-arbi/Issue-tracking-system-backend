package se.majp.cms.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import se.majp.cms.exception.BadRequestException;
import se.majp.cms.model.Issue;
import se.majp.cms.model.Story;
import se.majp.cms.repository.IssueRepository;
import se.majp.cms.repository.StoryRepository;

public class IssueServiceImpl implements IssueService
{
	@Autowired
	private IssueRepository issueRepository;

	@Autowired
	private StoryRepository storyRepository;

	@Override
	public Issue updateIssue(Issue issue, String storyId)
	{
		Story story = storyRepository.findByStoryId(storyId);

		if (issue.getIssueId() == null)
		{
			throw new BadRequestException("Issue not added, use add instead");
		}
		else if (story == null)
		{
			throw new EntityNotFoundException("No story found with that storyId");
		}

		Issue issueFromDb = issueRepository.findByIssueId(issue.getIssueId());
		issueFromDb.setTitle(issue.getTitle());
		issueFromDb.setDescription(issue.getDescription());

		return issueRepository.save(issueFromDb);
	}
}
