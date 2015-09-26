package se.majp.cms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_story")
public class Story extends AbstractEntity
{
	@Column(unique = true)
	private String storyId;

	private String name;
	private String description;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Enumerated(EnumType.STRING)
	private Priority priority;

	@ManyToOne
	private Project project;

	@ManyToOne
	private User user;

	@OneToMany(mappedBy = "story", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Collection<Issue> issues;

	@CreatedDate
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;

	protected Story()
	{
	}

	public Story(String storyId, String name, String description, Project project, Status status, Priority priority)
	{
		this.storyId = storyId;
		this.name = name;
		this.description = description;
		this.project = project;
		this.status = status;
		this.priority = priority;
		this.issues = new ArrayList<>();
	}

	public Story(String storyId, String name, String description, Status status, Priority priority)
	{
		this(storyId, name, description, null, status, priority);
	}

	public Story(String name, String description, Status status, Priority priority)
	{
		this(null, name, description, null, status, priority);
	}

	public String getStoryId()
	{
		return storyId;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}

	public Project getProject()
	{
		return project;
	}
	
	public void setProject(Project project)
	{
		this.project = project;
	}

	public Status getStatus()
	{
		return status;
	}

	public Story changeStatus(Status status)
	{
		this.status = status;
		return this;
	}

	public Priority getPriority()
	{
		return priority;
	}
	
	public void setPriority(Priority priority)
	{
		this.priority = priority;
	}

	public Collection<Issue> getIssues()
	{
		return issues;
	}
	
	public void setIssues(Collection<Issue> issues)
	{
		this.issues = issues;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
}
