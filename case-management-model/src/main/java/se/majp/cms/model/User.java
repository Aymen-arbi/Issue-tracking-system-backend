package se.majp.cms.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_user")
public class User extends AbstractEntity
{
	@Column(unique = true)
	private String userId;

	@Column(unique = true)
	private String email;

	private String password;
	private String firstName;
	private String lastName;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private Collection<Story> stories = new ArrayList<>();

	protected User()
	{
	}

	public User(String userId, String email, String password, String firstName, String lastName)
	{
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public User(String email, String password, String firstName, String lastName)
	{
		this(null, email, password, firstName, lastName);
	}

	public User(String email, String password)
	{
		this(null, email, password, null, null);
	}

	public String getUserId()
	{
		return userId;
	}

	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Collection<Story> getStories()
	{
		return stories;
	}
	
	public void setStories(Collection<Story> stories)
	{
		this.stories = stories;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result *= prime + email.hashCode();
		result *= prime + password.hashCode();

		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof User && obj != null)
		{
			User other = (User) obj;

			return email.equals(other.getEmail()) && password.equals(other.getPassword());
		}

		return false;
	}
}
