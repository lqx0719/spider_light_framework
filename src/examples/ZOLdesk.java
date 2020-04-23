package examples;

import configuration.Config;
import framework.Framework;
import javafx.util.Pair;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import request.Request;
import response.Response;
import response.Result;
import spider.Spider;
import utils.DBUtils;
import utils.FrameworkUtils;
import webpageResult.WebpageResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZOLdesk extends Spider {
    public ZOLdesk(String name, Connection connection){
        super(name,connection);
        this.startUrls("http://desk.zol.com.cn/fengjing/","http://desk.zol.com.cn/dongman/",
                "http://desk.zol.com.cn/meinv/","http://desk.zol.com.cn/chuangyi/",
                "http://desk.zol.com.cn/katong/","http://desk.zol.com.cn/qiche/",
                "http://desk.zol.com.cn/youxi/","http://desk.zol.com.cn/keai/",
                "http://desk.zol.com.cn/mingxing/","http://desk.zol.com.cn/jianzhu/",
                "http://desk.zol.com.cn/zhiwu/","http://desk.zol.com.cn/dongwu/",
                "http://desk.zol.com.cn/jingwu/","http://desk.zol.com.cn/yingshi/",
                "http://desk.zol.com.cn/chemo/","http://desk.zol.com.cn/model/",
                "http://desk.zol.com.cn/tiyu/","http://desk.zol.com.cn/shouchaobao/",
                "http://desk.zol.com.cn/meishi/","http://desk.zol.com.cn/xingzuo/",
                "http://desk.zol.com.cn/jieri/","http://desk.zol.com.cn/pinpai/",
                "http://desk.zol.com.cn/beijing/","http://desk.zol.com.cn/qita/",);
    }

    @Override
    public void onStart(Config config){
        this.addWebpageResult(new WebpageResult<Map<String,String>>() {
            @Override
            public void process(Map<String,String> item, Request request) {
                String img_type = "";
                String img_url = "";
                for(Map.Entry<String,String> entry:item.entrySet()){
                    img_type = entry.getKey();
                    img_url = entry.getValue();
                }
                String pic_url = img_url.replace("960x600","1920x1080");
                FrameworkUtils.downImages(request.getSpider().getConfig().getFilePath()+
                        File.separator+request.getSpider().getName()+File.separator
                        + img_type,pic_url);

                /*保存url到本机*/
                /*
                File downloadPath = new File(request.getSpider().getConfig().getFilePath()+
                        File.separator+request.getSpider().getName());
                File downloadFile = new File(downloadPath+File.separator
                        + img_type+".txt");
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
                    bw.write(pic_url+"\n");
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                 */
            }

        });
        for(Request request:this.requests){
            request.contentType("text/html; charset=gbk");
            request.charset("gbk");
        }
    }


    @Override
    protected Result parse(Response response) {
        String url = response.getRequest().getUrl();
        if(url.indexOf("bizhi") < 0){
            //存储壁纸专辑的URL
            String url_head = url.split("//")[1].split("/")[0];
            Elements elements = response.body().css(".photo-list-padding .pic");
            String pic_type = response.body().css(".choosebox dl").get(0).select(".sel").text();
            Result result = new Result();
            for(Element element:elements){
                Request request = this.makeRequest("http://"+url_head+element.attr("href"));
                //记录壁纸的类型，比如风景、创意、卡通
                request.setInfo(pic_type);
                result.addRequest(request);
            }
            //next page
            /*
            elements = response.body().css("#pageNext");
            if(null!=elements){
                for(Element e:elements){
                    String nextUrl = "http://"+url_head+elements.attr("href");

                    //只取前5页
                    if(Integer.parseInt(nextUrl.split("//")[1]
                            .split("/")[2].split("\\.")[0]) > 5){
                        continue;
                    }
                    result.addRequest(this.makeRequest(nextUrl));
                }
            }
            */

            return result;
        }else{
            //存储壁纸的URL
            String url_head = url.split("//")[1].split("/")[0];
            String img_url = response.body().css("#bigImg").attr("src");
            String img_type = response.getInfo();
            Map<String,String> map = new HashMap<>();
            map.put(img_type,img_url);
            Result<Map<String,String>> result = new Result(map);
            String next_url = response.body().css("#photo-next > a").attr("href");
            if(next_url.indexOf(".html") > 0){
                Request request = this.makeRequest("http://"+url_head+next_url);
                request.setInfo(img_type);
                result.addRequest(request);
            }
            return  result;
        }
    }

    public static void main(String[] args){
        Connection connection = DBUtils.getConnection();
        ZOLdesk zoLdesk = new ZOLdesk("ZOL壁纸",connection);
        Framework.newFramework(zoLdesk,Config.newConfig()).start();
        DBUtils.closeConnection(connection);
    }
}
