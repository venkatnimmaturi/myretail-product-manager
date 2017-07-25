package myretail.product.service.handler;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import myretail.jest.client.model.common.ElasticStatus;
import myretail.product.api.model.Product;
import myretail.product.command.api.ProductEsCommand;
import myretail.product.command.response.ProductApiResponse;
import myretail.product.rsclient.api.RetrieveProductCommand;
import myretail.product.rsclient.model.Status;
import myretail.product.rsclient.model.Status.ResultStatus;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@RunWith(MockitoJUnitRunner.class)
public class ProductHandlerTest {

	@Mock
	RetrieveProductCommand productCommand;

	@Mock
	ProductEsCommand elasticSearchCommand;

	@InjectMocks
	ProductHandler handler;

	@Mock
	ThreadPoolExecutor executor;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveProductInfo() throws Exception {
		RetrieveProductCommandRequest request = RetrieveProductCommandRequest.builder().productId(new Long(5647))
				.build();
		RetrieveProductCommandResponse response = RetrieveProductCommandResponse.builder()
				.product(Product.builder().id(new Long(10)).name("mock").build())
				.status(Status.builder().resultStatus(ResultStatus.SUCCESS).build()).build();
		ProductApiResponse apiResponse = ProductApiResponse.builder()
				.product(Product.builder().id(new Long(10)).price(Money.of(CurrencyUnit.USD, 10.24)).build())
				.status(ElasticStatus.SUCCESS).build();

		Mockito.when(executor.submit(Mockito.any(Callable.class)))
				.thenAnswer(new Answer<Future<RetrieveProductCommandResponse>>() {
					public Future<RetrieveProductCommandResponse> answer(InvocationOnMock invocation) throws Throwable {
						Future<RetrieveProductCommandResponse> future = Mockito.mock(FutureTask.class);
						Mockito.when(future.get()).thenReturn(response);
						return future;
					}
				}).thenAnswer(new Answer<Future<ProductApiResponse>>() {
					public Future<ProductApiResponse> answer(InvocationOnMock invocation) throws Throwable {
						Future<ProductApiResponse> future = Mockito.mock(FutureTask.class);
						Mockito.when(future.get()).thenReturn(apiResponse);
						return future;
					}
				});
		handler.retrieveProductInfo(request);

	}
}
