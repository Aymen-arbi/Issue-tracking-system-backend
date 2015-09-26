package se.majp.cms.repository.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RepositorySuite.class, ServiceSuite.class })
public class AllRepositoryAndServiceTests
{

}
