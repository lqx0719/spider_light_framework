package processorQueue;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import request.Request;
import response.Response;

public class processorQueue {
	BlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();
	BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();
	
	public void addRequest(Request request) {
		try {
			this.requestQueue.put(request);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addResponse(Response response) {
		try {
			this.responseQueue.put(response);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean hasRequest() {
		return requestQueue.size() > 0;
	}
	
	public boolean hasResponse() {
		return responseQueue.size() > 0;
	}
	
	public Request nextRequest() {
		try {
			return requestQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Response nextResponse() {
		try {
			return responseQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void addRequests(List<Request> requests) {
		for(Request request:requests) {
			this.addRequest(request);
		}
	}
	
	public void clear() {
		requestQueue.clear();
	}
}
