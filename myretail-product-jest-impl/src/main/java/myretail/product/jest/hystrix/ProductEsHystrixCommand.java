package myretail.product.jest.hystrix;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Update;
import myretail.jest.client.hystrix.AbstractJestHystrixCommand;
import myretail.jest.client.model.common.ElasticStatus;
import myretail.jest.client.model.common.ElasticsearchProperties;
import myretail.product.api.model.Product;
import myretail.product.jest.call.request.RetrieveProductCallRequest;
import myretail.product.jest.call.response.ProductCallResponse;
import myretail.product.jest.call.response.ProductCallResponse.ProductCallResponseBuilder;

@Component
public class ProductEsHystrixCommand
		extends AbstractJestHystrixCommand<RetrieveProductCallRequest, ProductCallResponse> {

	@Autowired
	ElasticsearchProperties properties;

	@Override
	@HystrixCommand(fallbackMethod = "defaultResponse", commandProperties = {
			@HystrixProperty(name = "requestCache.enabled", value = "false"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000") })
	public ProductCallResponse executeJestCall(RetrieveProductCallRequest request) {
		ProductCallResponseBuilder builder = ProductCallResponse.builder();

		switch (request.getOperation()) {

		case READ:

			Get get = (Get) request.getJestAction();
			try {
				JestResult result = jestClient.execute(get);
				if (result.isSucceeded()) {
					Product product = result.getSourceAsObject(Product.class);
					builder.result(product).status(ElasticStatus.SUCCESS);
				} else {
					builder.status(ElasticStatus.ERROR);
				}
			} catch (IOException e) {
				builder.status(ElasticStatus.ELASTICSEARCH_EXCEPTION);
			}
			break;

		case UPDATE:

			try {
				Update update = (Update) request.getJestAction();
				JestResult result = jestClient.execute(update);
				if (result.isSucceeded()) {
					Product product = result.getSourceAsObject(Product.class);
					builder.result(product).status(ElasticStatus.SUCCESS);
				} else {
					builder.status(ElasticStatus.ERROR);
				}
			} catch (IOException e) {
				builder.status(ElasticStatus.ELASTICSEARCH_EXCEPTION);
			}

			break;

		default:
			break;
		}

		return builder.build();
	}

	public ProductCallResponse defaultResponse(RetrieveProductCallRequest callRequest, Throwable t) {
		ProductCallResponseBuilder responseBuilder = ProductCallResponse.builder();
		responseBuilder.status(ElasticStatus.EMPTY_RESULT);
		return responseBuilder.build();
	}

}
