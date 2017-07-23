package myretail.product.command.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.searchbox.core.Get;
import io.searchbox.core.Update;
import myretail.jest.client.api.impl.AbstractElasticsearchApiCommand;
import myretail.jest.client.model.common.ElasticStatus;
import myretail.jest.client.model.common.ElasticsearchProperties;
import myretail.product.command.api.ProductEsCommand;
import myretail.product.command.request.ProductApiRequest;
import myretail.product.command.response.ProductApiResponse;
import myretail.product.command.response.ProductApiResponse.ProductApiResponseBuilder;
import myretail.product.jest.call.request.RetrieveProductCallRequest;
import myretail.product.jest.call.request.RetrieveProductCallRequest.Operation;
import myretail.product.jest.call.request.RetrieveProductCallRequest.RetrieveProductCallRequestBuilder;
import myretail.product.jest.call.response.ProductCallResponse;
import myretail.product.jest.hystrix.ProductEsHystrixCommand;

@Component
public class RetrieveProductEsCommandImpl extends
		AbstractElasticsearchApiCommand<RetrieveProductCallRequest, ProductCallResponse, ProductApiRequest, ProductApiResponse>
		implements ProductEsCommand {

	@Autowired
	ElasticsearchProperties properties;

	public RetrieveProductEsCommandImpl(ProductEsHystrixCommand hystrixCommand) {
		super(hystrixCommand);
	}

	@Override
	protected RetrieveProductCallRequest preProcess(ProductApiRequest apiRequest) {
		RetrieveProductCallRequestBuilder builder = RetrieveProductCallRequest.builder();

		switch (apiRequest.getAction()) {
		case READ:
			Get search = new Get.Builder(properties.getIndexName(), apiRequest.getProduct().getId().toString())
					.type(properties.getTypeName()).build();
			builder.jestAction(search);
			builder.operation(Operation.READ);
			break;

		case UPDATE:
			StringBuilder scriptBuilder = new StringBuilder();
			scriptBuilder.append("{\n");
			scriptBuilder.append("\"script\" : \"ctx._source.price.value = ");
			scriptBuilder.append(apiRequest.getProduct().getPrice().getAmount());
			scriptBuilder.append(";");
			scriptBuilder.append("ctx._source.price.currency_code = \\");
			scriptBuilder.append("\"");
			scriptBuilder.append(apiRequest.getProduct().getPrice().getCurrencyUnit());
			scriptBuilder.append("\\");
			scriptBuilder.append("\"");
			scriptBuilder.append("\"");
			scriptBuilder.append("\n}");
			Update update = new Update.Builder(scriptBuilder.toString()).index(properties.getIndexName())
					.type(properties.getTypeName()).id(apiRequest.getProduct().getId().toString()).build();
			builder.jestAction(update);
			builder.operation(Operation.UPDATE);
			break;

		default:
			break;
		}

		return builder.build();
	}

	@Override
	protected ProductCallResponse executeJestCall(RetrieveProductCallRequest callRequest) {
		return hystrixCommand.executeJestCall(callRequest);
	}

	@Override
	protected ProductApiResponse postProcess(ProductCallResponse callResponse) {

		ProductApiResponseBuilder responseBuilder = ProductApiResponse.builder();
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
