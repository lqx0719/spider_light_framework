package framework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import configuration.Config;
import event.Event;
import event.EventManager;
import processorQueue.processorQueue;
import request.Request;
import response.Response;
import response.Result;
import spider.Spider;
import webpageDownloader.WebpageDownloader;
import webpageResult.WebpageResult;

public class frameworkEngine {
	private List<Spider> spiders;
	private Config config;
	private boolean isRunning;
	private processorQueue processorQueue;
	private ExecutorService executorService;
	
	public frameworkEngine(Framework framework) {
	    
		this.spiders = framework.spiders;
		this.config = framework.config;
		this.processorQueue = new processorQueue();
		this.executorService = new ThreadPoolExecutor(config.maxDownloaderThreads(), config.maxDownloaderThreads(), 
				0, TimeUnit.MILLISECONDS, config.queueSize() == 0 ? new SynchronousQueue<>()
                        : (config.queueSize() < 0 ? new LinkedBlockingQueue<>()
                        : new LinkedBlockingQueue<>(config.queueSize())),Executors.defaultThreadFactory());
	}
	
	public void start() {
	    EventManager.startEvent(Event.GLOBAL_STARTED, config);
		if(isRunning) {
			throw new RuntimeException("爬虫引擎已经启动了！");
		}
		isRunning = true;
		for(Spider spider:spiders) {
			Config configer = config.clone();
			System.out.println("爬虫任务 [" + spider.getName() + "] 开始！！");
			spider.setConfig(configer);
			List<Request> requests = new ArrayList<>();
			for(String Url:spider.getStartUrls()) {
				requests.add(spider.makeRequest(Url));
			}
			spider.getRequests().addAll(requests);
			processorQueue.addRequests(requests);
			EventManager.startEvent(Event.SPIDER_STARTED, configer);
		}
		
		Thread downloadThread = new Thread() {
			@Override
			public void run() {
				while(isRunning) {
					if(!processorQueue.hasRequest()) {
						try {
							TimeUnit.MILLISECONDS.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Request request = processorQueue.nextRequest();
					
					executorService.submit(new WebpageDownloader(request,processorQueue));
					
					try {
						TimeUnit.MILLISECONDS.sleep(request.getSpider().getConfig().delay());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		downloadThread.setDaemon(true);
		downloadThread.setName("download-thread");
        downloadThread.start();
        this.run();
	}
	
	public void stop() {
		isRunning = false;
		processorQueue.clear();
		System.out.println("爬虫引擎关闭！！！");
	}
	
	public void run() {
		while(isRunning) {
			if(!processorQueue.hasResponse()) {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			Response response = processorQueue.nextResponse();
			Result result = response.getRequest().getParser().parse(response);
			List<Request> nextRequests = result.getRequests();
			if(null != nextRequests) {
			    nextRequests.forEach(request -> processorQueue.addRequest(request));
			}
			
			if(null != result.getItem()) {
			    List<WebpageResult> webpageResults = response.getRequest().getSpider().getWebpageResults();
			    for(WebpageResult webpageResult:webpageResults) {
			        webpageResult.process(result.getItem(), response.getRequest());
			    }
			}
		}
	}
}


