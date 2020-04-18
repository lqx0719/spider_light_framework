package examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import configuration.Config;
import framework.Framework;
import request.Request;
import response.Response;
import response.Result;
import spider.Spider;
import utils.FrameworkUtils;
import webpageResult.WebpageResult;

public class GZrecruitment extends Spider{
    public GZrecruitment(String name) {
        super(name);
        this.startUrls(
                "https://www.gz91.com/JOBsearch/?keyword=文员",
                "https://www.gz91.com/JOBsearch/?keyword=会计",
                "https://www.gz91.com/JOBsearch/?keyword=销售");
    }
    
    @Override
    public void onStart(Config config) {
        // TODO Auto-generated method stub
        this.addWebpageResult(new WebpageResult<List<String>>() {

            @Override
            public void process(List<String> item, Request request){
                // TODO Auto-generated method stub
                System.out.println("网页   " + request.getUrl() + " 内容下载完毕！！");
                File downloadPath = new File(request.getSpider().getConfig().getFilePath()+
                        File.separator+request.getSpider().getName());
                File downloadFile = new File(downloadPath+File.separator
                        +FrameworkUtils.getKeywordFromUrl(request.getUrl(), "keyword")+".txt");
                if(!downloadPath.exists()){
                    downloadPath.mkdirs();
                }
                if(!downloadFile.exists()) {
                    try {
                        downloadFile.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } 
                
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(downloadFile,true));
                    for(String s:item) {
                        bw.write(s+"\n");
                    }
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
    
    @Override
    public Result parse(Response response) {
        // TODO Auto-generated method stub
        Elements elements = response.body().css("#tgleft > li");
        List<String> infos = new ArrayList<>();
        for(Element element:elements) {
            String job = element.select(".zwtitle_1").text();
            String company = element.select(".zw_2").text();
            String place = element.select(".zw_3").text();
            String salary = element.select(".zw_4").text();
            infos.add(job+ "\t" + company + "\t" + place + "\t" + salary);
        }  
        Result<List<String>> result = new Result<List<String>>(infos);
        
        //获取后十页
        for(int i=2;i<11;i++) {
            int page = response.getRequest().getUrl().indexOf("page");
            if(page == -1) {
                String nextUrl = response.getRequest().getUrl() + "&page=" + i;
                Request nextRequest = this.makeRequest(nextUrl);
                result.addRequest(nextRequest);
            }
            
        }
        return result;
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
       GZrecruitment douban = new GZrecruitment("九一人才网");
       Framework.newFramework(douban, Config.newConfig()).start();
    }

}
