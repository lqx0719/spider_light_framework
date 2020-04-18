package request;

import java.util.HashMap;
import java.util.Map;

import spider.Spider;

public class Request<T> {
    private Spider spider;
	private String url;
    private Parser<T> parser;
    private String contentType = "text/html; charset=UTF-8";
    private String method = "GET";
    private String charset = "UTF-8";
    private Map<String,String> headers = new HashMap<>();
    private Map<String,String> cookies = new HashMap<>();
    
	public Request(Spider spider,String url,Parser<T> parser) {
		this.spider = spider;
        this.url = url;
        this.parser = parser;
        this.header("User-Agent", spider.getConfig().userAgent());
	}
	
	public Request header(String key,String value) {
	    this.headers.put(key,value);
	    return this;
	}
	
	public String header(String key) {	    
	    return this.headers.get(key);
	}
	
	public Request cookie(String key,String value) {
	    this.cookies.put(key,value);
        return this;
	}
	
	public String cookie(String key) {     
        return this.cookies.get(key);
    }
	
	public String charset() {
	    return this.charset;
	}
	
	public Request charset(String charset) {
	    this.charset = charset;
	    return this;
	}
	
	public Request contentType(String contentType) {
	    this.contentType = contentType;
	    return this;
	}
	
	public String contentType() {
	    return this.contentType;
	}
	
	public String method() {
	    return this.method;
	}
	
	public Request method(String method) {
	    this.method = method;
	    return this;
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

	public Map<String, String> getHeaders(){
	    return headers;
	}
}
