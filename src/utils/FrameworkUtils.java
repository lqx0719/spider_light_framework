package utils;

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
}
