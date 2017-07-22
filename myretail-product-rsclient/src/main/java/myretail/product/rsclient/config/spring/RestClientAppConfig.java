package myretail.product.rsclient.config.spring;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;

@Configuration
@EnableConfigurationProperties(RestServiceEndpointProperties.class)
@ComponentScan(basePackages = { "myretail.product.rsclient.hystrix", "myretail.product.rsclient.api" })
public class RestClientAppConfig {

	@Autowired
	@Setter
	private RestServiceEndpointProperties endpointConfig;

	@PostConstruct
	public void init() {
		// sanity check
		Assert.notNull(endpointConfig, "End point Configutation is null");
		Assert.notNull(endpointConfig.getUrl(), "End point is null");

	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
		jsonMessageConverter.setObjectMapper(objectMapper);
		messageConverters.add(jsonMessageConverter);
		restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}

}
