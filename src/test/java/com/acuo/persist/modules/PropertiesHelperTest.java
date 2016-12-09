package com.acuo.persist.modules;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.common.util.GuiceJUnitRunner.GuiceModules;
import com.acuo.persist.configuration.PropertiesHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;

import static org.junit.Assert.assertNotNull;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationTestModule.class })
public class PropertiesHelperTest {

	@Inject
	@Named(PropertiesHelper.NEO4J_OGM_URL)
	String neo4jOgmUrl;

	@Inject
	@Named(PropertiesHelper.NEO4J_OGM_USERNAME)
	String neo4jOgmUsername;

	@Inject
	@Named(PropertiesHelper.NEO4J_OGM_PASSWORD)
	String neo4jOgmPassword;

	@Inject
	@Named(PropertiesHelper.NEO4J_OGM_DRIVER)
	String neo4jOgmDriver;

	@Inject
	@Named(PropertiesHelper.NEO4J_OGM_PACKAGES)
	String neo4jOgmPackages;

	@Inject
	@Named(PropertiesHelper.ACUO_DATA_DIR)
	String acuoDataDir;

	@Test
	public void test() {
		assertNotNull(neo4jOgmUrl);
		assertNotNull(neo4jOgmDriver);
		assertNotNull(neo4jOgmUsername);
		assertNotNull(neo4jOgmPassword);
		assertNotNull(neo4jOgmPackages);
		assertNotNull(acuoDataDir);
	}

}
