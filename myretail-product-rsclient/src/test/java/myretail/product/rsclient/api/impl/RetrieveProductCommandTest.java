package myretail.product.rsclient.api.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import myretail.product.rsclient.hystrix.RetrieveProductHystrixCommand;
import myretail.product.rsclient.model.Status;
import myretail.product.rsclient.model.Status.ResultStatus;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveProductCommandTest {

	@Mock
	RetrieveProductHystrixCommand hystrixCommand;

	@InjectMocks
	RetrieveProductCommandImpl command;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExecuteSuccess() {
		RetrieveProductCommandResponse successResponse = RetrieveProductCommandResponse.builder()
				.status(Status.builder().resultStatus(ResultStatus.SUCCESS).build()).build();
		Mockito.when(hystrixCommand.makeRestCall(Mockito.any())).thenReturn(successResponse);
		RetrieveProductCommandRequest request = RetrieveProductCommandRequest.builder().build();
		RetrieveProductCommandResponse response = command.execute(request);
		Assert.assertEquals(ResultStatus.SUCCESS, response.getStatus().getResultStatus());
	}
	
	@Test
	public void testExecuteFailed() {
		RetrieveProductCommandResponse successResponse = RetrieveProductCommandResponse.builder()
				.status(Status.builder().resultStatus(ResultStatus.FAIL).build()).build();
		Mockito.when(hystrixCommand.makeRestCall(Mockito.any())).thenReturn(successResponse);
		RetrieveProductCommandRequest request = RetrieveProductCommandRequest.builder().build();
		RetrieveProductCommandResponse response = command.execute(request);
		Assert.assertEquals(ResultStatus.FAIL, response.getStatus().getResultStatus());
	}
}
