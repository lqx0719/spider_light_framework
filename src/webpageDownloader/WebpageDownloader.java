package webpageDownloader;

import processorQueue.processorQueue;
import request.Request;

public class WebpageDownloader implements Runnable{
	private final Request request;
	private final processorQueue processorQueue;
	
	public WebpageDownloader(Request request,processorQueue processorQueue) {
		this.request = request;
		this.processorQueue = processorQueue;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(request.getUrl()+"  开始请求！");
	}

}
