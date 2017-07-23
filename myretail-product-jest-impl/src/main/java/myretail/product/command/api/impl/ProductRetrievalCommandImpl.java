package myretail.product.command.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.searchbox.core.Get;
import myretail.jest.client.api.impl.AbstractElasticsearchApiCommand;
import myretail.jest.client.model.common.ElasticStatus;
import myretail.jest.client.model.common.ElasticsearchProperties;
import myretail.product.command.api.ProductRetrievalCommand;
import myretail.product.command.request.ProductRetrievalApiRequest;
import myretail.product.command.response.ProductRetrievalApiResponse;
import myretail.product.command.response.ProductRetrievalApiResponse.ProductRetrievalApiResponseBuilder;
import myretail.product.jest.call.request.ProductRetrievalCallRequest;
import myretail.product.jest.call.request.ProductRetrievalCallRequest.ProductRetrievalCallRequestBuilder;
import myretail.product.jest.call.response.ProductRetrievalCallResponse;
import myretail.product.jest.hystrix.ProductRetrievalHystrixCommand;

@Component
public class ProductRetrievalCommandImpl extends
		AbstractElasticsearchApiCommand<ProductRetrievalCallRequest, ProductRetrievalCallResponse, ProductRetrievalApiRequest, ProductRetrievalApiResponse>
		implements ProductRetrievalCommand {

	@Autowired
	ElasticsearchProperties properties;

	public ProductRetrievalCommandImpl(ProductRetrievalHystrixCommand hystrixCommand) {
		super(hystrixCommand);
	}

	@Override
	protected ProductRetrievalCallRequest preProcess(ProductRetrievalApiRequest apiRequest) {
		ProductRetrievalCallRequestBuilder builder = ProductRetrievalCallRequest.builder();
		Get search = new Get.Builder(properties.getIndexName(), apiRequest.getId().toString())
				.type(properties.getTypeName()).build();
		builder.search(search);
		return builder.build();
	}

	@Override
	protected ProductRetrievalCallResponse executeJestCall(ProductRetrievalCallRequest callRequest) {
		return hystrixCommand.executeJestCall(callRequest);
	}

	@Override
	protected ProductRetrievalApiResponse postProcess(ProductRetrievalCallResponse callResponse) {

		ProductRetrievalApiResponseBuilder responseBuilder = ProductRetrievalApiResponse.builder();
		switch (callResponse.getStatus()) {
		case ERROR:
			responseBuilder.status(ElasticStatus.ERROR);
			break;
		case ELASTICSEARCH_EXCEPTION:
			responseBuilder.status(ElasticStatus.ELASTICSEARCH_EXCEPTION);
			break;
		case SUCCESS:
			responseBuilder.status(ElasticStatus.SUCCESS);
			responseBuilder.product(callResponse.getResult());
			break;

		default:
			break;
		}
		return responseBuilder.build();
	}

}
