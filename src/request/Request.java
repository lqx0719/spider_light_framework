package request;

import spider.Spider;

public class Request<T> {
    private Spider spider;
	private String url;
    private Parser<T> parser;
    
	public Request(Spider spider,String url,Parser<T> parser) {
		this.spider = spider;
        this.url = url;
        this.parser = parser;
	}
	
	public Spider getSpider() {
		return spider;
	}

	public void setSpider(Spider spider) {
		this.spider = spider;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Parser<T> getParser() {
		return parser;
	}

	public void setParser(Parser<T> parser) {
		this.parser = parser;
	}

}
