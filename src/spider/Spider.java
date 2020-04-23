package spider;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import configuration.Config;
import event.EventManager;
import event.Event;
import request.Parser;
import request.Request;
import response.Response;
import response.Result;
import webpageResult.WebpageResult;


public abstract class Spider {
    protected String name;
    protected Connection connection;
    protected Config config;
    protected List<String> startUrls = new ArrayList<>();
    protected List<WebpageResult> webpageResults = new ArrayList<>();
    protected List<Request>  requests  = new ArrayList<>();

    public Spider(String name,Connection connection) {
        this.name = name;
        this.connection = connection;
        EventManager.RegistEvent(Event.SPIDER_STARTED, this::onStart);
    }

    /*
     * 将原始的URLS存入数组
     * */
    public Spider startUrls(String... urls) {
        this.startUrls.addAll(Arrays.asList(urls));
        return this;
    }

    /*
     * 爬虫开始前的提示
     * */
    public void onStart(Config config) {

    }

    protected <T> Spider addWebpageResult(WebpageResult<T> webpageResult) {
        this.webpageResults.add(webpageResult);
        return this;
    }

    /*
     * 将URL构建为Request对象
     * */
    public <T> Request<T> makeRequest(String url){
        return makeRequest(url, this::parse);
    }

    public <T> Request<T> makeRequest(String url,Parser<T> parser){
        return new Request(this,url,parser);
    }

    /*用来解析处理爬虫的结果*/
    protected abstract <T> Result<T> parse(Response response);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public List<String> getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(List<String> startUrls) {
        this.startUrls = startUrls;
    }

    public List<WebpageResult> getWebpageResults() {
        return webpageResults;
    }

    public void setWebpageResults(List<WebpageResult> webpageResults) {
        this.webpageResults = webpageResults;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public Connection getConnection() {
        return connection;
    }
}


