package myretail.product.rsclient.api;

import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

public interface RetrieveProductCommand {

	RetrieveProductCommandResponse execute(RetrieveProductCommandRequest apiRequest);
}
