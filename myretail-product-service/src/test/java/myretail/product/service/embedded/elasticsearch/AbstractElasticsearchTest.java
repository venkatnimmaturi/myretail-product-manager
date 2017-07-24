package myretail.product.service.embedded.elasticsearch;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.assertj.core.util.Files;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractElasticsearchTest {

	private static final String ES_WORKING_DIR = "build/es";
	private static final String CLUSTER_NAME = "myretail.test.elasticsearch";

	private static Node node;

	@BeforeClass
	public static void startElasticsearch() throws Exception {

		removeOldDataDir(ES_WORKING_DIR + "/" + CLUSTER_NAME);

		node = new MyNode(
				Settings.builder().put("transport.type", "netty4").put("http.type", "netty4")
						.put("http.enabled", "true").put("path.home", ES_WORKING_DIR).build(),
				Arrays.asList(Netty4Plugin.class));
		node.start();

	}

	@AfterClass
	public static void stopElasticsearch() throws IOException {
		node.close();
	}

	private static void removeOldDataDir(String dir) {

		File dataDir = new File(dir);
		if (dataDir.exists()) {
			Files.delete(dataDir);
		}

	}

	private static class MyNode extends Node {
		public MyNode(Settings preparedSettings, Collection<Class<? extends Plugin>> classpathPlugins) {
			super(InternalSettingsPreparer.prepareEnvironment(preparedSettings, null), classpathPlugins);
		}
	}
}
