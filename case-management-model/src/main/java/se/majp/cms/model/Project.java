package se.majp.cms.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_project")
public class Project extends AbstractEntity
{
	@Column(unique = true)
	private String projectId;
	private String name;
	private String description;
	private Team team;

	@OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Collection<Story> backlog;

	protected Project()
	{
	}

	public Project(String projectId, String name, String description)
	{
		this.projectId = projectId;
		this.name = name;
		this.description = description;
		this.team = new Team();
		this.backlog = new ArrayList<>();
	}

	public Project(String name, String description)
	{
		this(null, name, description);
	}

	public String getProjectId()
	{
		return projectId;
	}

	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
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

	public Team getTeam()
	{
		return team;
	}
	
	public void setTeam(Team team)
	{
		this.team = team;
	}

	public Collection<Story> getBacklog()
	{
		return backlog;
	}
	
	public void setBacklog(Collection<Story> backlog)
	{
		this.backlog = backlog;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result *= prime + projectId.hashCode();
		result *= prime + name.hashCode();

		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Project && obj != null)
		{
			Project other = (Project) obj;

			return projectId.equals(other.getProjectId()) && name.equals(other.getName());
		}

		return false;
	}

}
