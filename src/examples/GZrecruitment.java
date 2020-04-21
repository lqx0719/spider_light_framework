package examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
import utils.DBUtils;
import utils.FrameworkUtils;
import webpageResult.WebpageResult;

public class GZrecruitment extends Spider{
    public GZrecruitment(String name,Connection connection) {
        super(name,connection);
        this.startUrls(
                "https://www.gz91.com/");
    }
    
    @Override
    public void onStart(Config config) {
        // TODO Auto-generated method stub
        this.addWebpageResult(new WebpageResult<List<String>>() {

            @Override
            public void process(List<String> item, Request request){
                // TODO Auto-generated method stub
                System.out.println("网页   " + request.getUrl() + " 内容下载完毕！！");
                System.out.println("插入成功！一共插入  "+DBUtils.executeSQL(request.getSpider().getConnection(),item)+"  条数据！！");

                /*
                * 保存到本机
                * */
                /*
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

                 */
            }
        });
    }

    @Override
    public Result parse(Response response) {
        // TODO Auto-generated method stub
        if(response.getRequest().getUrl().indexOf("?") < 0){
			//主网站，提取其中的热门关键字
			Elements elements = response.body().css(".hot_box > dl >dd");
			List<Request> requests = new ArrayList<>();
			for (Element e:elements){
				requests.add(this.makeRequest("https://www.gz91.com/JOBsearch/?keyword="+e.text()));
			}
			Result result = new Result();

			result.addRequest(requests);
			return result;
		}else{
        	//关键字查询的网站，保存查询的信息

			Elements elements = response.body().css("#tgleft > li");
			List<String> infos = new ArrayList<>();
			for(Element element:elements) {
				String job = element.select(".zwtitle_1").text();
				String company = element.select(".zw_2").text();
				String place = element.select(".zw_3").text();
				String salary = element.select(".zw_4").text();
				String sql = "insert into jiuyi_spider(jiuyi_keyword,jiuyi_job,jiuyi_company,jiuyi_place,jiuyi_salary) " +
						"values('"+
						FrameworkUtils.getKeywordFromUrl(response.getRequest().getUrl(), "keyword")+
						"','"+job+
						"','"+company+
						"','"+place+
						"','"+salary+"')";
//            String info = job+ "\t" + company + "\t" + place + "\t" + salary;
				infos.add(sql);
			}
			Result<List<String>> result = new Result<List<String>>(infos);

			/*
			* 比较前后两次的Result是否都是一样的，是的话就终止
			* */


			//获取下一页
			Elements pageButton = response.body().css(".nextpage_a");
			if(pageButton.size() != 0){
				String nextUrl = "";
				if(response.getRequest().getUrl().indexOf("&page=") < 0){
					nextUrl = response.getRequest().getUrl() + "&page=2";

				}else{
					int curPage = Integer.parseInt(response.getRequest().getUrl().substring(
							response.getRequest().getUrl().indexOf("&page=")+6
							,response.getRequest().getUrl().length()));
					//超过100页就停止
					if(curPage == 100) return result;
					nextUrl = response.getRequest().getUrl().substring(0,response.getRequest().getUrl().indexOf("&page=")+6)+
							Integer.toString(curPage + 1);
				}
				Request nextRequest = this.makeRequest(nextUrl);
				result.addRequest(nextRequest);
			}

			return result;
		}

    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
       Connection connection = DBUtils.getConnection();
       GZrecruitment douban = new GZrecruitment("九一人才网",connection);
       Framework.newFramework(douban, Config.newConfig()).start();
       DBUtils.closeConnection(connection);
    }

}
