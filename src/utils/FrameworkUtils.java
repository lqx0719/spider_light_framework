package utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FrameworkUtils {
    public static String getKeywordFromUrl(String url,String keyword) {
        String[] paramsStr = url.substring(url.indexOf("?") + 1, url.length()).split("&");
        Map<String,String> params = new HashMap<String, String>();
        for(String paramStr:paramsStr) {
            String key = paramStr.split("=")[0];
            String val = paramStr.split("=")[1];
            params.put(key, val);
        }
        return params.get(keyword);
    }

    public static void downImages(String filePath, String imgUrl) {

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] listFiles = dir.listFiles();
        int fileCount = 0;
        for(File file:listFiles){
            if(!file.isDirectory()) fileCount++;
        }

        String fileName = fileCount +"."+
                imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length()).split("\\.")[1];

        File file = new File(filePath + File.separator + fileName);

        try {

            URL url = new URL(imgUrl);

            URLConnection connection = url.openConnection();

            connection.setConnectTimeout(10 * 1000);

            InputStream in = connection.getInputStream();

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

            byte[] buf = new byte[1024];
            int size;
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
