package myretail.product.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import myretail.product.api.model.Product;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.service.handler.ProductHandler;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class ProductController {

	@Autowired
	ProductHandler productHandler;

	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProductInfo(@PathVariable("id") long id) {

		log.debug("Initiating Request to find product details for {id}:" + id);
		return productHandler.retrieveProductInfo(RetrieveProductCommandRequest.builder().productId(id).build());
	}
}
