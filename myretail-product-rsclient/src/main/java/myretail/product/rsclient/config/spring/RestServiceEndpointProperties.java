package myretail.product.rsclient.config.spring;

import javax.annotation.PostConstruct;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties("myretail.product.rsclient.endpoint")
public class RestServiceEndpointProperties {

	private String url;
	private String authUser;
	private String encryptedPassword;
	private int connectionTimeout;

	@PostConstruct
	public void init() {
		Assert.isTrue(new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS).isValid(url),
				"'url' configuration parameter: " + url + "must be a valid URL");
	}
}
