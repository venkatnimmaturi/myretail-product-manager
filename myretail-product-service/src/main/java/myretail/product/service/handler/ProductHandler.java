package myretail.product.service.handler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import myretail.product.api.model.Product;
import myretail.product.api.model.Product.ProductBuilder;
import myretail.product.command.api.ProductRetrievalCommand;
import myretail.product.command.request.ProductRetrievalApiRequest;
import myretail.product.command.response.ProductRetrievalApiResponse;
import myretail.product.rsclient.api.RetrieveProductCommand;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@Component
@Slf4j
public class ProductHandler {

	@Autowired
	RetrieveProductCommand productCommand;

	@Autowired
	ProductRetrievalCommand elasticSearchCommand;

	@Autowired
	ThreadPoolExecutor taskExecutor;

	public ResponseEntity<Product> retrieveProductInfo(RetrieveProductCommandRequest request) {

		ResponseEntity<Product> consolidatedResponse = null;
		ProductBuilder productBuilder = Product.builder();
		ProductRetrievalApiRequest elasticSearchRequest = ProductRetrievalApiRequest.builder()
				.id(request.getProductId()).build();

		Future<RetrieveProductCommandResponse> futureRestResponse = taskExecutor
				.submit(new ProductRetrieverRestClientTask(request));

		Future<ProductRetrievalApiResponse> futureEsResponse = taskExecutor
				.submit(new ProductRetrieverElasticsearchClientTask(elasticSearchRequest));
		try {
			RetrieveProductCommandResponse restResponse = futureRestResponse.get();
			productBuilder.id(request.getProductId()).name(restResponse.getProduct().getName());
			ProductRetrievalApiResponse esResponse = futureEsResponse.get();
			productBuilder.price(esResponse.getProduct().getPrice());
			consolidatedResponse = new ResponseEntity<>(productBuilder.build(), HttpStatus.OK);
		} catch (InterruptedException | ExecutionException e) {
			log.debug("Exception occurred when fetching product info", e.getMessage());
			consolidatedResponse = new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
		}
		return consolidatedResponse;

	}

	private class ProductRetrieverRestClientTask implements Callable<RetrieveProductCommandResponse> {
		RetrieveProductCommandRequest restClientRequest;

		public ProductRetrieverRestClientTask(RetrieveProductCommandRequest restClientRequest) {
			this.restClientRequest = restClientRequest;
		}

		@Override
		public RetrieveProductCommandResponse call() throws Exception {
			return productCommand.execute(restClientRequest);
		}

	}

	private class ProductRetrieverElasticsearchClientTask implements Callable<ProductRetrievalApiResponse> {
		ProductRetrievalApiRequest esRequest;

		public ProductRetrieverElasticsearchClientTask(ProductRetrievalApiRequest esRequest) {
			this.esRequest = esRequest;
		}

		@Override
		public ProductRetrievalApiResponse call() throws Exception {
			return elasticSearchCommand.execute(esRequest);
		}

	}
}
