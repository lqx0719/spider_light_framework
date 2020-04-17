package framework;

import java.util.ArrayList;
import java.util.List;

import configuration.Config;
import spider.Spider;

public class Framework {
	List<Spider> spiders = new ArrayList<>();
	Config config;
	
	public Framework() {
		
	}
	public static Framework newFramework(Spider spider,Config config) {
		Framework framework = new Framework();
		framework.spiders.add(spider);
		framework.config = config;
		return framework;
	}
	
	public void start() {
		new frameworkEngine(this).start();
	}
}
