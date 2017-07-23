package myretail.product.jest.hystrix;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import myretail.jest.client.hystrix.AbstractJestHystrixCommand;
import myretail.jest.client.model.common.ElasticStatus;
import myretail.jest.client.model.common.ElasticsearchProperties;
import myretail.product.api.model.Product;
import myretail.product.jest.call.request.ProductRetrievalCallRequest;
import myretail.product.jest.call.response.ProductRetrievalCallResponse;
import myretail.product.jest.call.response.ProductRetrievalCallResponse.ProductRetrievalCallResponseBuilder;

@Component
public class ProductRetrievalHystrixCommand
		extends AbstractJestHystrixCommand<ProductRetrievalCallRequest, ProductRetrievalCallResponse> {

	@Autowired
	ElasticsearchProperties properties;

	@Override
	public ProductRetrievalCallResponse executeJestCall(ProductRetrievalCallRequest request) {
		ProductRetrievalCallResponseBuilder builder = ProductRetrievalCallResponse.builder();
		Get get = request.getSearch();
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
		return builder.build();
	}

}
