package myretail.product.service.acceptance;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import myretail.product.service.TestApplication;

@ContextConfiguration(loader = SpringBootContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { TestApplication.class, RunCukesTest.class })
@ActiveProfiles("junit")
public class AbstractDefs {

	protected int port;

	@Autowired
	private WebApplicationContext webApplicationContext;

	protected MockMvc mockMvc;

	@PostConstruct
	public void init() {

		if (webApplicationContext instanceof EmbeddedWebApplicationContext) {
			this.port = ((EmbeddedWebApplicationContext) webApplicationContext).getEmbeddedServletContainer().getPort();
		}
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}
}
