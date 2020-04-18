package response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Body {
    private String bodyString;
    private String charset;
    private InputStream inputStream;
    
    public Body(InputStream inputStream,String charset) {
        this.inputStream = inputStream;
        this.charset = charset;
    }
    
    @Override
    public String toString() {
        if(null == bodyString) {
            StringBuilder htmlString = new StringBuilder(100);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,charset));
                String temp;
                while((temp = br.readLine()) != null) {
                    htmlString.append(temp).append("\n");
                }
                this.bodyString = htmlString.toString();
                
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return this.bodyString;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }
    
    public Elements css(String css) {
        return Jsoup.parse(this.toString()).select(css);
    }
}
