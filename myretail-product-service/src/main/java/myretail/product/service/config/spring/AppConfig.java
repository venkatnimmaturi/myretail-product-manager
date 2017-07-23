package myretail.product.service.config.spring;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "myretail.**.config.spring" })
public class AppConfig {

	@Bean
	public ThreadPoolExecutor taskExecutor() {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
		executor.setCorePoolSize(5);
		executor.setMaximumPoolSize(5);
		return executor;
	}
}
