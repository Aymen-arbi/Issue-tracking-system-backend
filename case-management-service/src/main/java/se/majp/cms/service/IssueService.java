package se.majp.cms.service;

import se.majp.cms.model.Issue;

public interface IssueService
{
	Issue updateIssue(Issue issue, String storyId);
}
