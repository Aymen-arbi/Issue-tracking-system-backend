package se.majp.cms.repository.test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import se.majp.cms.model.User;
import se.majp.cms.test.config.IntegrationTestBaseClass;

public class UserRepositoryIntegrationTest extends IntegrationTestBaseClass
{
	@Test
	public void findByFirstNameOrLastNameOrEmail_NoMatch_shouldReturnEmptyList()
	{
		List<User> users = userRepository.findByFirstNameOrLastNameOrEmail("NO MATCH");
		assertThat(users.size(), is(0));
	}

	@Test
	public void findByFirstNameOrLastNameOrEmail_OneMatch_shouldReturnListOfSizeOne()
	{
		List<User> users = userRepository.findByFirstNameOrLastNameOrEmail(USER_FIRSTNAME);
		assertThat(users.size(), is(1));
		assertThat(users.get(0), allOf(
				hasProperty("userId", is(USER_USERID)),
				hasProperty("email", is(USER_EMAIL)),
				hasProperty("password", is(USER_PASSWORD)),
				hasProperty("firstName", is(USER_FIRSTNAME)),
				hasProperty("lastName", is(USER_LASTNAME))));
	}

	@Test
	public void findByUserId_NoMatch_shouldReturnNull()
	{
		User user = userRepository.findByUserId("NO MATCH");
		Assert.assertNull(user);
	}

	@Test
	public void findByUserId_Match_shouldReturnUser()
	{
		User user = userRepository.findByUserId(USER_USERID);
		assertThat(user.getUserId(), is(USER_USERID));
	}

	@Test
	public void findByEmail_NoMatch_shouldReturnNull()
	{
		User user = userRepository.findByEmail("NO MATCH");
		Assert.assertNull(user);
	}

	@Test
	public void findByEmail_Match_shouldReturnUser()
	{
		User user = userRepository.findByEmail(USER_EMAIL);
		assertThat(user.getEmail(), is(USER_EMAIL));
	}

	@Test
	public void findByProject_NoMatch_shouldReturnEmptyList()
	{
		List<User> users = userRepository.findByProject("NO MATCH");
		assertThat(users.size(), is(0));
	}

	@Test
	public void findByProject_OneMatch_shouldReturnListOfSizeOne()
	{
		List<User> users = userRepository.findByProject(PROJECT_PROJECTID);
		assertThat(users.size(), is(1));
		assertThat(users.get(0), allOf(
				hasProperty("userId", is(USER_USERID)),
				hasProperty("email", is(USER_EMAIL)),
				hasProperty("password", is(USER_PASSWORD)),
				hasProperty("firstName", is(USER_FIRSTNAME)),
				hasProperty("lastName", is(USER_LASTNAME))));
	}
}
