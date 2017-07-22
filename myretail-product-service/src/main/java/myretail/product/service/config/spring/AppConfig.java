package myretail.product.service.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "myretail.**.config.spring" })
public class AppConfig {

}
