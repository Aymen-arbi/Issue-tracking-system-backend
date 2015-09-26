package se.majp.cms.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;

@Embeddable
public class Team
{
	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@MapKeyJoinColumn(name = "user_id")
	@CollectionTable(name = "tbl_project_user", joinColumns = @JoinColumn(name = "project_id") )
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Map<User, Role> users = new HashMap<>();

	public Map<User, Role> getUsers()
	{
		return users;
	}

	public Team addUser(User user, Role role)
	{
		users.put(user, role);
		return this;
	}

	public void removeUser(User user)
	{
		users.remove(user);
	}

	public void addAllUsers(Map<User, Role> usersToAdd)
	{
		users.putAll(usersToAdd);
	}

	public Role getRole(User user)
	{
		return users.get(user);
	}

	public boolean userHasRole(User user, Role role)
	{
		return users.get(user).equals(role);
	}
}
