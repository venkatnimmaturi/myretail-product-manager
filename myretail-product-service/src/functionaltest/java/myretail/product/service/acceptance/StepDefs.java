package myretail.product.service.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefs extends AbstractDefs {

	private ResultActions resultActions;

	private static String URI = "/products/{id}";

	@When("^client makes a call to GET /products with (\\d+)$")
	public void client_makes_a_call_to_GET_products_with(int pathVar) throws Throwable {
		resultActions = mockMvc.perform(get(URI, pathVar).accept(MediaType.APPLICATION_JSON_UTF8));
	}

	@Then("^client has (\\d+) \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" in the response$")
	public void client_has_in_the_response(int id, String name, String value, String currencyCode) throws Throwable {
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", equalTo(id))).andExpect(jsonPath("$.name", equalTo(name)))
				.andExpect(jsonPath("$.price.currency_code", equalTo(currencyCode)));
	}

}
