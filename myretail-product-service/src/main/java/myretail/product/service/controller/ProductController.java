package myretail.product.service.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Product> updateProductInfo(@PathVariable("id") Long id, @Valid @RequestBody Product product)
			throws MethodArgumentNotValidException, NoSuchMethodException, SecurityException {

		log.debug("Initiating update Request to update product price for {id}:" + id);
		if (product.getPrice().getAmount().intValue() <= 0) {
			BeanPropertyBindingResult errors = new BeanPropertyBindingResult(product.getPrice().getAmount(), "value");
			throw new MethodArgumentNotValidException(new MethodParameter(
					this.getClass().getDeclaredMethod("updateProductInfo", Long.class, Product.class), 0), errors);

		}
		return productHandler.updateProductInfo(id, product);
	}
}
