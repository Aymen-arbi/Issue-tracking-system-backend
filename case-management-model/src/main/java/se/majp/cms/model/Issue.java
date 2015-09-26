package se.majp.cms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_issue")
public class Issue extends AbstractEntity
{
	@Column(unique = true)
	private String issueId;

	private String title;
	private String description;

	@ManyToOne
	private Story story;

	protected Issue()
	{
	}

	public Issue(String issueId, String title, String description, Story story)
	{
		this.issueId = issueId;
		this.title = title;
		this.description = description;
		this.story = story;
	}

	public Issue(String issueId, String title, String description)
	{
		this(issueId, title, description, null);
	}

	public Issue(String title, String description)
	{
		this(null, title, description, null);
	}

	public String getIssueId()
	{
		return issueId;
	}

	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}

	public Story getStory()
	{
		return story;
	}
	
	public void setStory(Story story)
	{
		this.story = story;
	}
}
