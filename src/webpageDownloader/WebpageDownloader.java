package webpageDownloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import processorQueue.processorQueue;
import request.Request;
import response.Response;

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
		try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            if("get".equalsIgnoreCase(request.method())) {
                urlConnection.setRequestMethod("GET");
            }
            if("post".equalsIgnoreCase(request.method())) {
                urlConnection.setRequestMethod("POST");
            }
            urlConnection.setConnectTimeout(request.getSpider().getConfig().timeout());
            urlConnection.setReadTimeout(request.getSpider().getConfig().timeout());
            urlConnection.setRequestProperty("Charsert", request.charset());
            urlConnection.setRequestProperty("Content-type", request.contentType());
            Map<String,String> headers = request.getHeaders();
            for(String key : headers.keySet()) {
                urlConnection.setRequestProperty(key,headers.get(key));
            }
            InputStream inputStream = urlConnection.getInputStream();
            Response response = new Response(request, inputStream);
            processorQueue.addResponse(response);
            
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

}
