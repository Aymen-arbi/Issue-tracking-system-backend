package se.majp.cms.model;

public final class TeamMember
{
	private final User user;
	private final Role role;

	public TeamMember(User user, Role role)
	{
		this.user = user;
		this.role = role;
	}

	public User getUser()
	{
		return user;
	}

	public Role getRole()
	{
		return role;
	}
}
