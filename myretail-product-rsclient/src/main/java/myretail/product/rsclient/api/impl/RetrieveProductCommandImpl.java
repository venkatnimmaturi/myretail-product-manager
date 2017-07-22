package myretail.product.rsclient.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myretail.product.rsclient.api.RetrieveProductCommand;
import myretail.product.rsclient.hystrix.RetrieveProductHystrixCommand;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@Component("retrieveProductCommand")
public class RetrieveProductCommandImpl implements RetrieveProductCommand {

	@Autowired
	RetrieveProductHystrixCommand hystrixCommand;

	@Override
	public RetrieveProductCommandResponse execute(RetrieveProductCommandRequest apiRequest) {
		return hystrixCommand.makeRestCall(apiRequest);
	}

}
