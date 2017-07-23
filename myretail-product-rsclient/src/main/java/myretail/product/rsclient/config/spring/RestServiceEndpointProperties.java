package myretail.product.rsclient.config.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Primary
@Getter
@Setter
@ConfigurationProperties("myretail.product.rsclient.endpoint")
public class RestServiceEndpointProperties {

	private String url;
	private String authUser;
	private String encryptedPassword;
	private int connectionTimeout;

}
