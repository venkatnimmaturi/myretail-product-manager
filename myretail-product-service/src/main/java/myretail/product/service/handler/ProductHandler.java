package myretail.product.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myretail.product.rsclient.api.RetrieveProductCommand;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@Component
public class ProductHandler {

	@Autowired
	RetrieveProductCommand productCommand;

	public RetrieveProductCommandResponse retrieveProductInfo(RetrieveProductCommandRequest request) {
		return productCommand.execute(request);
	}
}
