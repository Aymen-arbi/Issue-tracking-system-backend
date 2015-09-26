package se.majp.cms.repository.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import se.majp.cms.service.test.ProjectServiceIntegrationTest;
import se.majp.cms.service.test.StoryServiceIntegrationTest;
import se.majp.cms.service.test.UserServiceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({ ProjectServiceIntegrationTest.class,
		UserServiceIntegrationTest.class,
		StoryServiceIntegrationTest.class
})
public class ServiceSuite
{
}
