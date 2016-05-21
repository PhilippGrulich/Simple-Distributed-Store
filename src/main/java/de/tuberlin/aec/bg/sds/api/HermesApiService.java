package de.tuberlin.aec.bg.sds.api;

import de.tub.ise.hermes.IRequestHandler;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.RequestHandlerRegistry;
import de.tub.ise.hermes.Response;
import de.tuberlin.aec.bg.sds.Operation;
public class HermesApiService extends APIService {
	public HermesApiService() {
		RequestHandlerRegistry.getInstance().registerHandler("api",new ApiRequestHandler());
	}
	
	private class ApiRequestHandler implements IRequestHandler{

		@Override
		public Response handleRequest(Request req) {
			Operation operation = (Operation)req.getItems().get(0);
			
			Boolean result = callback.apply(operation);
			
			return new Response("apiResponse", result, req, result);
			
		}

		@Override
		public boolean requiresResponse() {
			return true;
		}
		
		
	}
}
