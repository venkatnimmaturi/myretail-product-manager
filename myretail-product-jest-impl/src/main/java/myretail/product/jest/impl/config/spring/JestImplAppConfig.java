package myretail.product.jest.impl.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "myretail.product.command.api.impl", "myretail.product.jest.hystrix" })
public class JestImplAppConfig {

}
