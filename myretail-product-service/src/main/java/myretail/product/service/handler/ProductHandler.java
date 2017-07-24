package myretail.product.service.handler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import myretail.jest.client.model.common.ElasticStatus;
import myretail.product.api.model.Product;
import myretail.product.api.model.Product.ProductBuilder;
import myretail.product.command.api.ProductEsCommand;
import myretail.product.command.request.ProductApiRequest;
import myretail.product.command.request.ProductApiRequest.Action;
import myretail.product.command.response.ProductApiResponse;
import myretail.product.rsclient.api.RetrieveProductCommand;
import myretail.product.rsclient.model.Status.ResultStatus;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@Component
@Slf4j
public class ProductHandler {

	@Autowired
	RetrieveProductCommand productCommand;

	@Autowired
	ProductEsCommand elasticSearchCommand;

	@Autowired
	ThreadPoolExecutor taskExecutor;

	public ResponseEntity<Product> retrieveProductInfo(RetrieveProductCommandRequest request) {

		ResponseEntity<Product> consolidatedResponse = null;
		ProductBuilder productBuilder = Product.builder();
		Product requestObj = Product.builder().id(request.getProductId()).build();
		ProductApiRequest elasticSearchRequest = ProductApiRequest.builder().product(requestObj).action(Action.READ)
				.build();

		Future<RetrieveProductCommandResponse> futureRestResponse = taskExecutor
				.submit(new ProductRetrieverRestClientTask(request));

		Future<ProductApiResponse> futureEsResponse = taskExecutor
				.submit(new ProductRetrieverElasticsearchClientTask(elasticSearchRequest));
		try {
			productBuilder.id(request.getProductId());
			RetrieveProductCommandResponse restResponse = futureRestResponse.get();
			if (restResponse.getStatus() != null
					&& restResponse.getStatus().getResultStatus() == ResultStatus.SUCCESS) {
				productBuilder.name(restResponse.getProduct().getName());
			} else {
				buildDefaultResponse(productBuilder, true);
			}

			ProductApiResponse esResponse = futureEsResponse.get();
			if (esResponse != null && esResponse.getStatus() == ElasticStatus.SUCCESS) {
				productBuilder.price(esResponse.getProduct().getPrice());
			} else {
				buildDefaultResponse(productBuilder, false);
			}
			consolidatedResponse = new ResponseEntity<>(productBuilder.build(), HttpStatus.OK);
		} catch (InterruptedException | ExecutionException e) {
			log.debug("Exception occurred when fetching product info", e.getMessage());
			consolidatedResponse = new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		return consolidatedResponse;

	}

	/**
	 * Eventually, the default response is set by Hystrix's fallback method.
	 * 
	 * @param productBuilder
	 * @param restResponse
	 */
	private void buildDefaultResponse(ProductBuilder productBuilder, boolean restResponse) {

		if (restResponse) {
			productBuilder.name("Not Available");
		} else {
			productBuilder.price(Money.of(CurrencyUnit.of("USD"), 0.0));
		}
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

	private class ProductRetrieverElasticsearchClientTask implements Callable<ProductApiResponse> {
		ProductApiRequest esRequest;

		public ProductRetrieverElasticsearchClientTask(ProductApiRequest esRequest) {
			this.esRequest = esRequest;
		}

		@Override
		public ProductApiResponse call() throws Exception {
			return elasticSearchCommand.execute(esRequest);
		}

	}

	public ResponseEntity<Product> updateProductInfo(long id, Product product) {

		ResponseEntity<Product> response = null;
		Product requestObj = Product.builder().id(id).build();
		ProductApiRequest elasticSearchRequest = ProductApiRequest.builder().product(requestObj).action(Action.READ)
				.build();

		ProductApiResponse existingProductResponse = elasticSearchCommand.execute(elasticSearchRequest);
		if (existingProductResponse != null && existingProductResponse.getProduct() != null
				&& existingProductResponse.getProduct().getId() != null) {
			Product updatedProduct = existingProductResponse.getProduct();
			updatedProduct.setPrice(product.getPrice());
			ProductApiRequest elasticSearchUpdateRequest = ProductApiRequest.builder().product(updatedProduct)
					.action(Action.UPDATE).build();
			ProductApiResponse updatedProductResponse = elasticSearchCommand.execute(elasticSearchUpdateRequest);
			if (updatedProductResponse.getStatus() == ElasticStatus.SUCCESS) {
				response = new ResponseEntity<Product>(updatedProductResponse.getProduct(), HttpStatus.OK);
			} else {
				response = new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} else {
			response = new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}

		return response;
	}
}
