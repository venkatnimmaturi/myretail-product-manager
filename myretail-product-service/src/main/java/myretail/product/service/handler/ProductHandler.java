package myretail.product.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myretail.product.command.api.ProductRetrievalCommand;
import myretail.product.command.request.ProductRetrievalApiRequest;
import myretail.product.command.response.ProductRetrievalApiResponse;
import myretail.product.rsclient.api.RetrieveProductCommand;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@Component
public class ProductHandler {

	@Autowired
	RetrieveProductCommand productCommand;

	@Autowired
	ProductRetrievalCommand elasticSearchCommand;

	public RetrieveProductCommandResponse retrieveProductInfo(RetrieveProductCommandRequest request) {
		RetrieveProductCommandResponse response = productCommand.execute(request);

		ProductRetrievalApiRequest elasticSearchRequest = ProductRetrievalApiRequest.builder()
				.id(request.getProductId()).build();
		ProductRetrievalApiResponse esResponse = elasticSearchCommand.execute(elasticSearchRequest);
		response.getProduct().setPrice(esResponse.getProduct().getPrice());
		return response;

	}
}
