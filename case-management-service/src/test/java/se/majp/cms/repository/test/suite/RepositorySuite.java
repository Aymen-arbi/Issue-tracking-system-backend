package se.majp.cms.repository.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import se.majp.cms.repository.test.ProjectRepositoryIntegrationTest;
import se.majp.cms.repository.test.StoryRepositoryIntegrationTest;
import se.majp.cms.repository.test.UserRepositoryIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({
		ProjectRepositoryIntegrationTest.class,
		UserRepositoryIntegrationTest.class,
		StoryRepositoryIntegrationTest.class
})
public class RepositorySuite
{

}
