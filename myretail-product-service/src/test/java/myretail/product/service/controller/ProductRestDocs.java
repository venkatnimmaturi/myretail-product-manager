package myretail.product.service.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import myretail.product.api.model.Product;
import myretail.product.service.controller.advice.MethodArgumentTypeMismatchExceptionHandler;
import myretail.product.service.handler.ProductHandler;

public class ProductRestDocs {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("src/main/restdocs");

	@InjectMocks
	private ProductController productController;

	private MockMvc mockMvc;

	ResponseEntity<Product> mockResponse;

	@Mock
	ProductHandler productHandler;

	private final Snippet productResponseSnippet = responseFields(fieldWithPath("id").description("Product ID"),
			fieldWithPath("name").description("Product Name"),
			fieldWithPath("price.value").description("Product Price"),
			fieldWithPath("price.currency_code").description("Currency Code"));

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController)
				.setControllerAdvice(new MethodArgumentTypeMismatchExceptionHandler())
				.apply(documentationConfiguration(this.restDocumentation)).build();
	}

	@Test
	public void getProductInfoSuccess() throws Exception {
		Product mockProduct = Product.builder().id(new Long(10)).name("Mock Product")
				.price(Money.of(CurrencyUnit.of("USD"), 10.30)).build();
		mockResponse = new ResponseEntity<>(mockProduct, HttpStatus.OK);
		Mockito.when(productHandler.retrieveProductInfo(Mockito.any())).thenReturn(mockResponse);

		mockMvc.perform(get("/products/{id}", 10).accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("retrieve-product-info", preprocessRequest(removeHeaders("host"), prettyPrint()),
						preprocessResponse(prettyPrint()), this.productResponseSnippet));
	}

	@Test
	public void getProductInfoProductNotFound() throws Exception {
		mockResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Mockito.when(productHandler.retrieveProductInfo(Mockito.any())).thenReturn(mockResponse);

		mockMvc.perform(get("/products/{id}", 10).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

	}

	@Test
	public void getProductInfoWhenRestTemplateIsFailed() throws Exception {
		Product mockProduct = Product.builder().id(new Long(10)).name("Not Available")
				.price(Money.of(CurrencyUnit.of("USD"), 10.30)).build();
		mockResponse = new ResponseEntity<>(mockProduct, HttpStatus.OK);
		Mockito.when(productHandler.retrieveProductInfo(Mockito.any())).thenReturn(mockResponse);

		mockMvc.perform(get("/products/{id}", 10).accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("retrieve-product-info-default-name",
						preprocessRequest(removeHeaders("host"), prettyPrint()), preprocessResponse(prettyPrint()),
						this.productResponseSnippet));
	}

	@Test
	public void getProductInfoWhenElasticsearchIsFailed() throws Exception {
		Product mockProduct = Product.builder().id(new Long(10)).name("Mock Produc")
				.price(Money.of(CurrencyUnit.of("USD"), 0.0)).build();
		mockResponse = new ResponseEntity<>(mockProduct, HttpStatus.OK);
		Mockito.when(productHandler.retrieveProductInfo(Mockito.any())).thenReturn(mockResponse);

		mockMvc.perform(get("/products/{id}", 10).accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("retrieve-product-info-default-price",
						preprocessRequest(removeHeaders("host"), prettyPrint()), preprocessResponse(prettyPrint()),
						this.productResponseSnippet));
	}

	@Test
	public void updateProductDetailsSuccess() throws Exception {
		Product mockProduct = Product.builder().id(new Long(10)).name("Mock Product")
				.price(Money.of(CurrencyUnit.of("USD"), 15.30)).build();
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(mockProduct);
		mockResponse = new ResponseEntity<>(mockProduct, HttpStatus.OK);
		Mockito.when(productHandler.updateProductInfo(Mockito.any(Long.class), Mockito.any())).thenReturn(mockResponse);

		mockMvc.perform(put("/products/{id}", 10).contentType(MediaType.APPLICATION_JSON_UTF8).content(str))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("update-product-info", preprocessRequest(removeHeaders("host"), prettyPrint()),
						preprocessResponse(prettyPrint()), this.productResponseSnippet));
	}

	@Test
	public void updateProductDetailsElasticsearchDown() throws Exception {
		Product mockProduct = Product.builder().id(new Long(10)).name("Mock Product")
				.price(Money.of(CurrencyUnit.of("USD"), 15.30)).build();
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(mockProduct);
		mockResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(productHandler.updateProductInfo(Mockito.any(Long.class), Mockito.any())).thenReturn(mockResponse);

		mockMvc.perform(put("/products/{id}", 10).contentType(MediaType.APPLICATION_JSON_UTF8).content(str))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void updateProductDetailsBadRequest() throws Exception {
		Product mockProduct = Product.builder().id(new Long(10)).name("Mock Product").build();
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(mockProduct);
		mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Mockito.when(productHandler.updateProductInfo(Mockito.any(Long.class), Mockito.any())).thenReturn(mockResponse);

		mockMvc.perform(put("/products/{id}", 10).contentType(MediaType.APPLICATION_JSON_UTF8).content(str))
				.andExpect(status().isBadRequest());

	}

}
